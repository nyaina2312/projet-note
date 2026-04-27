package com.exemple.projet.service;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.exemple.projet.model.Demande;
import com.exemple.projet.model.DemandeStatut;
import com.exemple.projet.model.Statut;
import com.exemple.projet.model.Travaux;
import com.exemple.projet.model.HoraireTravail;
import com.exemple.projet.model.JourNonTravaille;
import com.exemple.projet.model.Alerte;
import com.exemple.projet.repository.DemandeRepository;
import com.exemple.projet.repository.DemandeStatutRepository;
import com.exemple.projet.repository.StatutRepository;
import com.exemple.projet.repository.TravauxRepository;
import com.exemple.projet.repository.DevisRepository;
import com.exemple.projet.repository.HoraireTravailRepository;
import com.exemple.projet.repository.JourNonTravailleRepository;
import com.exemple.projet.repository.AlerteRepository;

@Service
public class DemandeServiceImpl implements DemandeService {

    @Autowired
    private DemandeRepository demandeRepository;

    @Autowired
    private TravauxRepository travauxRepository;

    @Autowired
    private DemandeStatutRepository demandeStatutRepository;

    @Autowired
    private StatutRepository statutRepository;

    @Autowired
    private DevisRepository devisRepository;

    @Autowired
    private HoraireTravailRepository horaireTravailRepository;

    @Autowired
    private JourNonTravailleRepository jourNonTravailleRepository;

    @Autowired
    private AlerteRepository alerteRepository;

    @Value("${seuil.alerte.duree.heures:72}")
    private double seuilAlerteHeures;

    @Override
    public List<Demande> findAll() {
        return demandeRepository.findAll();
    }

    @Override
    public List<Demande> findAllWithClient() {
        return demandeRepository.findAllWithClient();
    }

    @Override
    public Optional<Demande> findById(Integer id) {
        return demandeRepository.findById(id);
    }

    @Override
    public Demande save(Demande demande) {
        System.out.println("DEBUG: save called - demande.id=" + demande.getId());
        Demande savedDemande = demandeRepository.save(demande);

        if (savedDemande.getId() != null) {
            Optional<Travaux> existingTravaux = travauxRepository.findByDemandeId(savedDemande.getId());
            if (!existingTravaux.isPresent()) {
                System.out.println("DEBUG: Creating travaux for demande " + savedDemande.getId());
                Travaux travaux = new Travaux();
                travaux.setDemande(savedDemande);
                travaux.setTypeStatut(null);
                Travaux savedTravaux = travauxRepository.save(travaux);
                travauxRepository.flush();

                Statut statutEnAttente = statutRepository.findById(1).orElse(null);
                if (statutEnAttente != null) {
                    DemandeStatut demandeStatut = new DemandeStatut();
                    demandeStatut.setDemande(savedDemande);
                    demandeStatut.setStatut(statutEnAttente);
                    demandeStatut.setDateChangement(new Date());
                    demandeStatut.setTravaux(savedTravaux);
                    demandeStatutRepository.save(demandeStatut);
                    demandeStatutRepository.flush();
                    System.out.println("DEBUG: Created DemandeStatut with id=" + demandeStatut.getId());
                }
            } else {
                System.out.println("DEBUG: Travaux already exists for demande " + savedDemande.getId());
                List<DemandeStatut> allStats = demandeStatutRepository.findAll();
                boolean hasStatut = allStats.stream()
                    .anyMatch(s -> s.getDemande() != null && s.getDemande().getId().equals(savedDemande.getId()));

                if (!hasStatut) {
                    System.out.println("DEBUG: Creating DemandeStatut for existing travaux");
                    Statut statutEnAttente = statutRepository.findById(1).orElse(null);
                    if (statutEnAttente != null) {
                        DemandeStatut demandeStatut = new DemandeStatut();
                        demandeStatut.setDemande(savedDemande);
                        demandeStatut.setStatut(statutEnAttente);
                        demandeStatut.setDateChangement(new Date());
                        demandeStatut.setTravaux(existingTravaux.get());
                        demandeStatutRepository.save(demandeStatut);
                        demandeStatutRepository.flush();
                        System.out.println("DEBUG: Created DemandeStatut with id=" + demandeStatut.getId());
                    }
                }
            }
        }

        return savedDemande;
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        try {
            List<com.exemple.projet.model.Devis> devisList = devisRepository.findByDemandeId(id);
            for (com.exemple.projet.model.Devis devis : devisList) {
                if (devis.getDetailsDevis() != null) {
                    for (com.exemple.projet.model.DetailsDevis detail : devis.getDetailsDevis()) {
                        // Supprimer via le repository si nécessaire
                    }
                }
                devisRepository.delete(devis);
            }

            Optional<Travaux> travauxOpt = travauxRepository.findByDemandeId(id);
            if (travauxOpt.isPresent()) {
                Travaux travaux = travauxOpt.get();
                List<DemandeStatut> historique = demandeStatutRepository.findByTravauxIdOrderByDateChangementDesc(travaux.getId());
                for (DemandeStatut ds : historique) {
                    demandeStatutRepository.delete(ds);
                }
                travauxRepository.delete(travaux);
            }

            demandeRepository.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la suppression: " + e.getMessage());
        }
    }

    @Override
    public Statut getStatutActuel(Integer demandeId) {
        try {
            List<DemandeStatut> allStats = demandeStatutRepository.findAll();
            final Integer demandeIdFinal = demandeId;
            DemandeStatut lastStatut = allStats.stream()
                .filter(s -> s.getDemande() != null && s.getDemande().getId().equals(demandeIdFinal))
                .sorted((s1, s2) -> s2.getDateChangement().compareTo(s1.getDateChangement()))
                .findFirst()
                .orElse(null);
            if (lastStatut != null) {
                return lastStatut.getStatut();
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    // ==================== MÉTHODES PRIVÉES ====================

    private DemandeStatut getDernierStatut(Integer demandeId) {
        List<DemandeStatut> allStats = demandeStatutRepository.findAll();
        return allStats.stream()
            .filter(s -> s.getDemande() != null && s.getDemande().getId().equals(demandeId))
            .sorted((s1, s2) -> s2.getDateChangement().compareTo(s1.getDateChangement()))
            .findFirst()
            .orElse(null);
    }

    private double calculerDureeCalendrier(Date dateDebut, Date dateFin) {
        if (dateDebut == null || dateFin == null) {
            return 0.0;
        }
        long diffMillis = dateFin.getTime() - dateDebut.getTime();
        return diffMillis / (1000.0 * 60 * 60);
    }

    private double calculerDureeOuvree(Date dateDebut, Date dateFin) {
        if (dateDebut == null || dateFin == null) {
            return 0.0;
        }

        LocalDateTime start = dateDebut.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime end = dateFin.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        if (start.isAfter(end)) {
            return 0.0;
        }

        HoraireTravail horaire = horaireTravailRepository.findFirstBy();
        LocalTime heureDebut = (horaire != null) ? horaire.getHeureDebut() : LocalTime.of(9, 0);
        LocalTime heureFin   = (horaire != null) ? horaire.getHeureFin()   : LocalTime.of(17, 0);

        List<JourNonTravaille> joursNonTravailles = jourNonTravailleRepository
                .findByDateJourBetween(start.toLocalDate(), end.toLocalDate());

        java.util.Set<LocalDate> datesInterdites = new java.util.HashSet<>();
        for (JourNonTravaille j : joursNonTravailles) {
            datesInterdites.add(j.getDateJour());
        }

        double totalHeures = 0.0;
        LocalDate currentDate = start.toLocalDate();

        while (!currentDate.isAfter(end.toLocalDate())) {
            DayOfWeek jourSemaine = currentDate.getDayOfWeek();
            if (jourSemaine == DayOfWeek.SATURDAY || jourSemaine == DayOfWeek.SUNDAY) {
                currentDate = currentDate.plusDays(1);
                continue;
            }

            if (datesInterdites.contains(currentDate)) {
                currentDate = currentDate.plusDays(1);
                continue;
            }

            LocalTime debutJour, finJour;

            if (currentDate.isEqual(start.toLocalDate()) && currentDate.isEqual(end.toLocalDate())) {
                debutJour = start.toLocalTime().isAfter(heureDebut) ? start.toLocalTime() : heureDebut;
                finJour   = end.toLocalTime().isBefore(heureFin) ? end.toLocalTime() : heureFin;
            } else if (currentDate.isEqual(start.toLocalDate())) {
                debutJour = start.toLocalTime().isAfter(heureDebut) ? start.toLocalTime() : heureDebut;
                finJour   = heureFin;
            } else if (currentDate.isEqual(end.toLocalDate())) {
                debutJour = heureDebut;
                finJour   = end.toLocalTime().isBefore(heureFin) ? end.toLocalTime() : heureFin;
            } else {
                debutJour = heureDebut;
                finJour   = heureFin;
            }

            if (debutJour.isBefore(finJour)) {
                long minutes = Duration.between(debutJour, finJour).toMinutes();
                totalHeures += minutes / 60.0;
            }

            currentDate = currentDate.plusDays(1);
        }

        return totalHeures;
    }

    private void creerAlerteSiDepasseSeuil(Demande demande, double dureeCalendrierHeures, Date dateChangement) {
        if (dureeCalendrierHeures > seuilAlerteHeures) {
            Alerte alerte = new Alerte();
            alerte.setDemande(demande);
            alerte.setTypeAlerte("DEPASSEMENT_DELAI");
            alerte.setMessage(String.format(
                "Le traitement de la demande #%d a duré %.2f heures (seuil: %.2f h) - dépassement détecté le %s",
                demande.getId(), dureeCalendrierHeures, seuilAlerteHeures, dateChangement
            ));
            alerte.setDateCreation(new Date());
            alerte.setStatut(Alerte.StatutAlerte.NON_LUE);
            alerteRepository.save(alerte);
        }
    }

    // ==================== MÉTHODES DE CHANGEMENT DE STATUT MODIFIÉES ====================

    @Override
    @Transactional
    public void changerStatut(Integer demandeId, Integer nouveauStatutId) {
        Optional<Travaux> travauxOpt = travauxRepository.findByDemandeId(demandeId);
        if (travauxOpt.isPresent()) {
            Travaux travaux = travauxOpt.get();

            Optional<Statut> nouveauStatutOpt = statutRepository.findById(nouveauStatutId);
            if (nouveauStatutOpt.isPresent()) {
                Statut nouveauStatut = nouveauStatutOpt.get();
                Date maintenant = new Date();

                travaux.setTypeStatut(nouveauStatut.getTypeStatut());
                travauxRepository.save(travaux);

                DemandeStatut dernier = getDernierStatut(demandeId);
                double dureeCalendrier = 0.0;
                double dureeOuvree = 0.0;
                if (dernier != null) {
                    dureeCalendrier = calculerDureeCalendrier(dernier.getDateChangement(), maintenant);
                    dureeOuvree = calculerDureeOuvree(dernier.getDateChangement(), maintenant);
                }

                DemandeStatut demandeStatut = new DemandeStatut();
                demandeStatut.setDemande(travaux.getDemande());
                demandeStatut.setStatut(nouveauStatut);
                demandeStatut.setDateChangement(maintenant);
                demandeStatut.setTravaux(travaux);
                demandeStatut.setDureeTotaleHeures(dureeCalendrier);
                demandeStatut.setDureeOuvrableHeures(dureeOuvree);
                demandeStatutRepository.save(demandeStatut);

                creerAlerteSiDepasseSeuil(travaux.getDemande(), dureeCalendrier, maintenant);
            }
        }
    }

    @Override
    @Transactional
    public void changerStatutAvecObservation(Integer demandeId, Integer nouveauStatutId, String observation) {
        Optional<Travaux> travauxOpt = travauxRepository.findByDemandeId(demandeId);
        if (travauxOpt.isPresent()) {
            Travaux travaux = travauxOpt.get();

            Optional<Statut> nouveauStatutOpt = statutRepository.findById(nouveauStatutId);
            if (nouveauStatutOpt.isPresent()) {
                Statut nouveauStatut = nouveauStatutOpt.get();
                Date maintenant = new Date();

                travaux.setTypeStatut(nouveauStatut.getTypeStatut());
                travauxRepository.save(travaux);

                DemandeStatut dernier = getDernierStatut(demandeId);
                double dureeCalendrier = 0.0;
                double dureeOuvree = 0.0;
                if (dernier != null) {
                    dureeCalendrier = calculerDureeCalendrier(dernier.getDateChangement(), maintenant);
                    dureeOuvree = calculerDureeOuvree(dernier.getDateChangement(), maintenant);
                }

                DemandeStatut demandeStatut = new DemandeStatut();
                demandeStatut.setDemande(travaux.getDemande());
                demandeStatut.setStatut(nouveauStatut);
                demandeStatut.setDateChangement(maintenant);
                demandeStatut.setObservation(observation);
                demandeStatut.setTravaux(travaux);
                demandeStatut.setDureeTotaleHeures(dureeCalendrier);
                demandeStatut.setDureeOuvrableHeures(dureeOuvree);
                demandeStatutRepository.save(demandeStatut);

                creerAlerteSiDepasseSeuil(travaux.getDemande(), dureeCalendrier, maintenant);
            }
        }
    }

    @Override
    @Transactional
    public void changerStatutAvecDate(Integer demandeId, Integer nouveauStatutId, Date dateChangement) {
        Optional<Travaux> travauxOpt = travauxRepository.findByDemandeId(demandeId);
        if (travauxOpt.isPresent()) {
            Travaux travaux = travauxOpt.get();

            Optional<Statut> nouveauStatutOpt = statutRepository.findById(nouveauStatutId);
            if (nouveauStatutOpt.isPresent()) {
                Statut nouveauStatut = nouveauStatutOpt.get();

                travaux.setTypeStatut(nouveauStatut.getTypeStatut());
                travauxRepository.save(travaux);

                DemandeStatut dernier = getDernierStatut(demandeId);
                double dureeCalendrier = 0.0;
                double dureeOuvree = 0.0;
                if (dernier != null) {
                    dureeCalendrier = calculerDureeCalendrier(dernier.getDateChangement(), dateChangement);
                    dureeOuvree = calculerDureeOuvree(dernier.getDateChangement(), dateChangement);
                }

                DemandeStatut demandeStatut = new DemandeStatut();
                demandeStatut.setDemande(travaux.getDemande());
                demandeStatut.setStatut(nouveauStatut);
                demandeStatut.setDateChangement(dateChangement);
                demandeStatut.setTravaux(travaux);
                demandeStatut.setDureeTotaleHeures(dureeCalendrier);
                demandeStatut.setDureeOuvrableHeures(dureeOuvree);
                demandeStatutRepository.save(demandeStatut);

                creerAlerteSiDepasseSeuil(travaux.getDemande(), dureeCalendrier, dateChangement);
            }
        }
    }

    // ==================== MÉTHODES D'ORIGINE (INCHANGÉES) ====================

    @Override
    @Transactional
    public void ajouterObservation(Integer demandeId, String observation) {
        System.out.println("DEBUG: ajouterObservation called - demandeId=" + demandeId + ", observation=" + observation);

        if (observation == null || observation.isEmpty()) {
            System.out.println("DEBUG: observation is empty, returning");
            return;
        }

        Optional<Demande> demandeOpt = demandeRepository.findById(demandeId);
        if (!demandeOpt.isPresent()) {
            System.out.println("DEBUG: Demande not found");
            return;
        }

        System.out.println("DEBUG: Demande found with id=" + demandeOpt.get().getId());

        List<DemandeStatut> allStats = demandeStatutRepository.findAll();
        System.out.println("DEBUG: Total DemandeStatut count: " + allStats.size());

        List<DemandeStatut> statsForDemande = allStats.stream()
            .filter(s -> s.getDemande() != null && s.getDemande().getId().equals(demandeId))
            .sorted((s1, s2) -> s2.getDateChangement().compareTo(s1.getDateChangement()))
            .collect(java.util.stream.Collectors.toList());

        System.out.println("DEBUG: statsForDemande count: " + statsForDemande.size());

        if (statsForDemande.isEmpty()) {
            System.out.println("DEBUG: No existing statut, cannot update");
            return;
        }

        DemandeStatut derniereLigne = statsForDemande.get(0);
        Statut statut = derniereLigne.getStatut();

        List<Travaux> travauxList = travauxRepository.findAll();
        System.out.println("DEBUG: Total travaux count: " + travauxList.size());

        Optional<Travaux> travauxOpt = travauxList.stream()
            .filter(t -> t.getDemande() != null && t.getDemande().getId().equals(demandeId))
            .findFirst();

        Travaux travaux;
        if (travauxOpt.isPresent()) {
            travaux = travauxOpt.get();
            System.out.println("DEBUG: Using existing travaux id=" + travaux.getId());
        } else {
            System.out.println("DEBUG: Creating new travaux");
            travaux = new Travaux();
            travaux.setDemande(demandeOpt.get());
            travaux.setTypeStatut(null);
            travaux = travauxRepository.save(travaux);
        }

        derniereLigne.setDateChangement(new Date());
        derniereLigne.setObservation(observation);
        demandeStatutRepository.save(derniereLigne);
        demandeStatutRepository.flush();
        System.out.println("DEBUG: Updated existing DemandeStatut (no new line created)");
    }

    // ==================== MÉTHODES DE L'INTERFACE (AJOUTÉES) ====================

    @Override
    public java.math.BigDecimal getMontantTotal(Integer demandeId) {
        List<com.exemple.projet.model.Devis> devisList = devisRepository.findByDemandeId(demandeId);
        java.math.BigDecimal total = java.math.BigDecimal.ZERO;
        for (com.exemple.projet.model.Devis devis : devisList) {
            if (devis.getMontantTotal() != null) {
                total = total.add(devis.getMontantTotal());
            }
        }
        return total;
    }

    @Override
    public java.util.Map<Integer, Long> getStatsParStatut() {
        java.util.Map<Integer, Long> stats = new java.util.HashMap<>();
        List<Demande> demandes = findAll();
        for (Demande demande : demandes) {
            Statut statut = getStatutActuel(demande.getId());
            if (statut != null) {
                stats.merge(statut.getId(), 1L, Long::sum);
            }
        }
        return stats;
    }

    @Override
    public long count() {
        return demandeRepository.count();
    }

    @Override
    public List<Statut> getAllStatuts() {
        return statutRepository.findAll();
    }

    @Override
    public List<Alerte> getAlertesNonLues() {
        return alerteRepository.findByStatut(Alerte.StatutAlerte.NON_LUE);
    }

    @Override
    public long countAlertesNonLues() {
        return alerteRepository.countByStatut(Alerte.StatutAlerte.NON_LUE);
    }

    @Override
    public void marquerAlerteLue(Integer alerteId) {
        Optional<Alerte> opt = alerteRepository.findById(alerteId);
        opt.ifPresent(a -> {
            a.setStatut(Alerte.StatutAlerte.LUE);
            alerteRepository.save(a);
        });
    }

    @Override
    public List<Alerte> getAlertesParDemande(Integer demandeId) {
        return alerteRepository.findByDemandeIdAndStatut(demandeId, Alerte.StatutAlerte.NON_LUE);
    }

    @Override
    public double getSeuilAlerteHeures() {
        return seuilAlerteHeures;
    }
}