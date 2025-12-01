package org.pentagone.business.zentracore.hr.ai.document.service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pentagone.business.zentracore.hr.ai.document.dto.DocumentGenerationRequest;
import org.pentagone.business.zentracore.hr.ai.document.dto.GeneratedDocumentDTO;
import org.pentagone.business.zentracore.hr.ai.document.entity.GeneratedDocument;
import org.pentagone.business.zentracore.hr.ai.document.repository.GeneratedDocumentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentGeneratorService {

    private final GeneratedDocumentRepository documentRepository;

    @Value("${ai.document.output-path:./uploads/generated-documents}")
    private String outputPath;

    @Transactional
    public GeneratedDocumentDTO generateDocument(DocumentGenerationRequest request) {
        log.info("Generating document type: {} for employee: {}",
                request.getDocumentType(), request.getEmployeeId());

        try {
            // Create output directory if it doesn't exist
            Path dirPath = Paths.get(outputPath);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }

            String fileName = generateFileName(request.getDocumentType(), request.getEmployeeId());
            String filePath = Paths.get(outputPath, fileName).toString();

            // Generate document based on type
            switch (request.getDocumentType().toUpperCase()) {
                case "CONTRACT":
                    generateContract(filePath, request);
                    break;
                case "ATTESTATION":
                    generateAttestation(filePath, request);
                    break;
                case "CERTIFICATE":
                    generateCertificate(filePath, request);
                    break;
                case "PAYSLIP":
                    generatePayslip(filePath, request);
                    break;
                default:
                    throw new IllegalArgumentException("Type de document non supporté: " + request.getDocumentType());
            }

            // Save record in database
            GeneratedDocument document = new GeneratedDocument();
            document.setDocumentType(request.getDocumentType());
            document.setEmployeeId(request.getEmployeeId());
            document.setEmployeeName(getEmployeeName(request));
            document.setFilePath(filePath);
            document.setFileName(fileName);
            document.setGeneratedAt(LocalDateTime.now());

            GeneratedDocument saved = documentRepository.save(document);

            log.info("Document generated successfully: {}", fileName);
            return convertToDTO(saved);

        } catch (Exception e) {
            log.error("Error generating document", e);
            throw new RuntimeException("Erreur lors de la génération du document: " + e.getMessage());
        }
    }

    private void generateContract(String filePath, DocumentGenerationRequest request) throws Exception {
        PdfWriter writer = new PdfWriter(new FileOutputStream(filePath));
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        try {
            // Header avec fond coloré
            com.itextpdf.layout.element.Table headerTable = new com.itextpdf.layout.element.Table(1);
            headerTable.setWidth(com.itextpdf.layout.properties.UnitValue.createPercentValue(100));

            com.itextpdf.layout.element.Cell headerCell = new com.itextpdf.layout.element.Cell();
            headerCell.setBackgroundColor(com.itextpdf.kernel.colors.ColorConstants.BLUE);
            headerCell.setPadding(15);
            headerCell.add(new Paragraph("CONTRAT DE TRAVAIL")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(20)
                    .setBold()
                    .setFontColor(com.itextpdf.kernel.colors.ColorConstants.WHITE));
            headerTable.addCell(headerCell);
            document.add(headerTable);

            document.add(new Paragraph("\n"));

            // Informations Entreprise
            document.add(new Paragraph("ENTREPRISE")
                    .setFontSize(14)
                    .setBold()
                    .setFontColor(com.itextpdf.kernel.colors.ColorConstants.BLUE));

            document.add(new Paragraph("Zentra - Solutions RH & Gestion d'Entreprise")
                    .setFontSize(11)
                    .setBold());
            document.add(new Paragraph("Adresse: Lot II M 34 Ampefiloha, Antananarivo 101, Madagascar")
                    .setFontSize(10));
            document.add(new Paragraph("SIRET: 123 456 789 00012")
                    .setFontSize(10));
            document.add(new Paragraph("Email: contact@zentra.mg | Tél: +261 20 22 XXX XX")
                    .setFontSize(10));

            document.add(new Paragraph("\n"));

            // Informations Employé
            String employeeName = getEmployeeName(request);
            String position = (String) request.getAdditionalData().getOrDefault("position", "Non spécifié");

            document.add(new Paragraph("EMPLOYÉ")
                    .setFontSize(14)
                    .setBold()
                    .setFontColor(com.itextpdf.kernel.colors.ColorConstants.BLUE));

            com.itextpdf.layout.element.Table employeeTable = new com.itextpdf.layout.element.Table(2);
            employeeTable.setWidth(com.itextpdf.layout.properties.UnitValue.createPercentValue(100));

            employeeTable.addCell(createTableCell("Nom complet:", true));
            employeeTable.addCell(createTableCell(employeeName, false));
            employeeTable.addCell(createTableCell("Poste:", true));
            employeeTable.addCell(createTableCell(position, false));

            document.add(employeeTable);
            document.add(new Paragraph("\n"));

            // Préambule
            document.add(new Paragraph("ENTRE LES SOUSSIGNÉS:")
                    .setFontSize(12)
                    .setBold()
                    .setUnderline());
            document.add(new Paragraph("\n"));

            // Corps du contrat
            String contractType = (String) request.getAdditionalData().getOrDefault("contractType", "CDI");
            String startDate = (String) request.getAdditionalData().getOrDefault("startDate",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

            document.add(createArticle("ARTICLE 1 - OBJET DU CONTRAT",
                    "Le présent contrat de travail à durée " + (contractType.equals("CDI") ? "indéterminée" : "déterminée") +
                    " a pour objet de définir les conditions dans lesquelles Monsieur/Madame " + employeeName +
                    " exercera ses fonctions au sein de la société Zentra.\n\n" +
                    "L'employé(e) s'engage à exercer ses fonctions avec compétence, assiduité et loyauté, " +
                    "dans le respect des règlements intérieurs et des instructions qui lui seront données par sa hiérarchie."));

            document.add(createArticle("ARTICLE 2 - DURÉE ET DATE D'EFFET",
                    "Type de contrat: " + getContractTypeLabel(contractType) + "\n" +
                    "Date de prise d'effet: " + startDate + "\n" +
                    (contractType.equals("CDD") && request.getAdditionalData().containsKey("endDate")
                        ? "Date de fin: " + request.getAdditionalData().get("endDate") + "\n"
                        : "") +
                    "\nCe contrat prendra effet à compter de la date susmentionnée. " +
                    "L'employé(e) s'engage à respecter les horaires de travail en vigueur dans l'entreprise."));

            document.add(createArticle("ARTICLE 3 - FONCTIONS",
                    "L'employé(e) exercera les fonctions de " + position + ".\n\n" +
                    "À ce titre, il/elle sera notamment chargé(e) de:\n" +
                    "• Assurer les missions qui lui sont confiées avec professionnalisme\n" +
                    "• Respecter les procédures et méthodes de travail établies\n" +
                    "• Collaborer efficacement avec les équipes et la hiérarchie\n" +
                    "• Contribuer à l'amélioration continue des processus\n\n" +
                    "Ces fonctions sont susceptibles d'évoluer en fonction des besoins de l'entreprise, " +
                    "dans le respect de la qualification professionnelle de l'employé(e)."));

            // Rémunération
            String salaryStr = request.getAdditionalData().getOrDefault("salary", "0").toString();
            double salary = Double.parseDouble(salaryStr);

            document.add(createArticle("ARTICLE 4 - RÉMUNÉRATION",
                    "En contrepartie de ses services, l'employé(e) percevra une rémunération brute mensuelle de:\n\n" +
                    "Salaire brut mensuel: " + String.format("%,.0f", salary) + " Ar\n\n" +
                    "Cette rémunération est versée mensuellement par virement bancaire, sous réserve des cotisations " +
                    "sociales et fiscales en vigueur. Un bulletin de paie détaillé sera remis mensuellement.\n\n" +
                    "La rémunération pourra être révisée annuellement lors de l'entretien d'évaluation, " +
                    "en fonction des performances et de l'évolution de l'entreprise."));

            document.add(createArticle("ARTICLE 5 - PÉRIODE D'ESSAI",
                    "Le présent contrat est assorti d'une période d'essai de " +
                    (contractType.equals("CDI") ? "trois (3) mois" : "un (1) mois") + " à compter de la date de prise d'effet, " +
                    "renouvelable une fois d'un commun accord.\n\n" +
                    "Durant cette période, chacune des parties pourra mettre fin au contrat à tout moment, " +
                    "sans indemnité ni préavis, sauf dispositions légales contraires."));

            document.add(createArticle("ARTICLE 6 - TEMPS DE TRAVAIL",
                    "La durée hebdomadaire de travail est de 40 heures, réparties du lundi au vendredi.\n\n" +
                    "Les horaires de travail sont fixés comme suit:\n" +
                    "• Matin: 08h00 - 12h00\n" +
                    "• Après-midi: 13h00 - 17h00\n\n" +
                    "Ces horaires peuvent être aménagés en fonction des nécessités du service, " +
                    "moyennant un préavis raisonnable."));

            document.add(createArticle("ARTICLE 7 - CONGÉS PAYÉS",
                    "L'employé(e) bénéficie de congés payés conformément aux dispositions légales en vigueur, " +
                    "soit 2,5 jours ouvrables par mois de travail effectif.\n\n" +
                    "Les dates de congés sont fixées d'un commun accord entre l'employeur et l'employé(e), " +
                    "en tenant compte des nécessités du service."));

            document.add(createArticle("ARTICLE 8 - CONFIDENTIALITÉ ET LOYAUTÉ",
                    "L'employé(e) s'engage à:\n" +
                    "• Respecter la confidentialité absolue de toutes les informations dont il/elle aura connaissance\n" +
                    "• Ne pas divulguer d'informations sensibles relatives à l'entreprise\n" +
                    "• Ne pas utiliser les informations confidentielles à des fins personnelles\n" +
                    "• Restituer tous documents et matériels confiés à la fin du contrat\n\n" +
                    "Cette obligation de confidentialité demeure après la cessation du contrat."));

            document.add(createArticle("ARTICLE 9 - RUPTURE DU CONTRAT",
                    (contractType.equals("CDI") ?
                    "Le présent contrat peut être rompu par l'une ou l'autre des parties moyennant le respect d'un préavis " +
                    "de deux (2) mois pour l'employeur et un (1) mois pour l'employé(e), " +
                    "sauf en cas de faute grave ou de force majeure." :
                    "Le contrat prendra automatiquement fin à la date prévue, sans qu'il soit nécessaire de notifier " +
                    "un préavis, sauf renouvellement expressément convenu entre les parties.")));

            document.add(createArticle("ARTICLE 10 - DISPOSITIONS GÉNÉRALES",
                    "Le présent contrat est régi par la législation malgache du travail en vigueur. " +
                    "Tout litige relatif à l'interprétation ou à l'exécution du présent contrat sera soumis " +
                    "aux juridictions compétentes d'Antananarivo.\n\n" +
                    "Le présent contrat est établi en deux (2) exemplaires originaux, dont un pour chaque partie."));

            document.add(new Paragraph("\n\n"));

            // Section signature
            document.add(new Paragraph("Fait à Antananarivo, le " +
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy", java.util.Locale.FRENCH)))
                    .setFontSize(10)
                    .setItalic());

            document.add(new Paragraph("\n\n"));

            com.itextpdf.layout.element.Table signatureTable = new com.itextpdf.layout.element.Table(2);
            signatureTable.setWidth(com.itextpdf.layout.properties.UnitValue.createPercentValue(100));

            com.itextpdf.layout.element.Cell leftCell = new com.itextpdf.layout.element.Cell();
            leftCell.setBorder(com.itextpdf.layout.borders.Border.NO_BORDER);
            leftCell.add(new Paragraph("L'Employeur")
                    .setFontSize(11)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER));
            leftCell.add(new Paragraph("\n\n\n"));
            leftCell.add(new Paragraph("Signature et cachet")
                    .setFontSize(9)
                    .setItalic()
                    .setTextAlignment(TextAlignment.CENTER));

            com.itextpdf.layout.element.Cell rightCell = new com.itextpdf.layout.element.Cell();
            rightCell.setBorder(com.itextpdf.layout.borders.Border.NO_BORDER);
            rightCell.add(new Paragraph("L'Employé(e)")
                    .setFontSize(11)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER));
            rightCell.add(new Paragraph("\n\n\n"));
            rightCell.add(new Paragraph("Signature précédée de la mention")
                    .setFontSize(9)
                    .setItalic()
                    .setTextAlignment(TextAlignment.CENTER));
            rightCell.add(new Paragraph("\"Lu et approuvé\"")
                    .setFontSize(9)
                    .setItalic()
                    .setTextAlignment(TextAlignment.CENTER));

            signatureTable.addCell(leftCell);
            signatureTable.addCell(rightCell);
            document.add(signatureTable);

        } finally {
            document.close();
        }
    }

    private Paragraph createArticle(String title, String content) {
        Paragraph article = new Paragraph();
        article.add(new Paragraph(title)
                .setFontSize(11)
                .setBold()
                .setFontColor(com.itextpdf.kernel.colors.ColorConstants.BLUE));
        article.add(new Paragraph(content)
                .setFontSize(10)
                .setTextAlignment(TextAlignment.JUSTIFIED)
                .setMarginLeft(10));
        article.add(new Paragraph("\n"));
        return article;
    }

    private com.itextpdf.layout.element.Cell createTableCell(String content, boolean isBold) {
        com.itextpdf.layout.element.Cell cell = new com.itextpdf.layout.element.Cell();
        cell.setPadding(5);
        Paragraph p = new Paragraph(content).setFontSize(10);
        if (isBold) {
            p.setBold();
        }
        cell.add(p);
        return cell;
    }

    private String getContractTypeLabel(String contractType) {
        switch (contractType.toUpperCase()) {
            case "CDI": return "Contrat à Durée Indéterminée (CDI)";
            case "CDD": return "Contrat à Durée Déterminée (CDD)";
            case "STAGE": return "Convention de Stage";
            case "ALTERNANCE": return "Contrat en Alternance";
            default: return contractType;
        }
    }

    private void generateAttestation(String filePath, DocumentGenerationRequest request) throws Exception {
        PdfWriter writer = new PdfWriter(new FileOutputStream(filePath));
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        try {
            // En-tête avec fond vert
            com.itextpdf.layout.element.Table headerTable = new com.itextpdf.layout.element.Table(1);
            headerTable.setWidth(com.itextpdf.layout.properties.UnitValue.createPercentValue(100));

            com.itextpdf.layout.element.Cell headerCell = new com.itextpdf.layout.element.Cell();
            headerCell.setBackgroundColor(new com.itextpdf.kernel.colors.DeviceRgb(76, 175, 80)); // Vert clair
            headerCell.setPadding(15);
            headerCell.add(new Paragraph("ATTESTATION DE TRAVAIL")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(20)
                    .setBold()
                    .setFontColor(com.itextpdf.kernel.colors.ColorConstants.WHITE));
            headerTable.addCell(headerCell);
            document.add(headerTable);

            document.add(new Paragraph("\n"));

            // Numéro d'attestation
            String attestationNumber = "ATT-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) +
                "-" + String.format("%04d", request.getEmployeeId());
            document.add(new Paragraph("N° " + attestationNumber)
                    .setFontSize(9)
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setItalic());

            document.add(new Paragraph("\n"));

            // En-tête entreprise avec encadré
            com.itextpdf.layout.element.Table companyTable = new com.itextpdf.layout.element.Table(1);
            companyTable.setWidth(com.itextpdf.layout.properties.UnitValue.createPercentValue(100));

            com.itextpdf.layout.element.Cell companyCell = new com.itextpdf.layout.element.Cell();
            companyCell.setBorder(new com.itextpdf.layout.borders.SolidBorder(
                com.itextpdf.kernel.colors.ColorConstants.GRAY, 1));
            companyCell.setPadding(10);
            companyCell.setBackgroundColor(new com.itextpdf.kernel.colors.DeviceRgb(245, 245, 245));

            companyCell.add(new Paragraph("ZENTRA - Solutions RH & Gestion d'Entreprise")
                    .setFontSize(12)
                    .setBold()
                    .setFontColor(com.itextpdf.kernel.colors.ColorConstants.BLUE));
            companyCell.add(new Paragraph("Lot II M 34 Ampefiloha, Antananarivo 101, Madagascar")
                    .setFontSize(9));
            companyCell.add(new Paragraph("SIRET: 123 456 789 00012 | Email: contact@zentra.mg | Tél: +261 20 22 XXX XX")
                    .setFontSize(9));

            companyTable.addCell(companyCell);
            document.add(companyTable);

            document.add(new Paragraph("\n\n"));

            // Corps de l'attestation
            String employeeName = getEmployeeName(request);
            String position = (String) request.getAdditionalData().getOrDefault("position", "");
            String startDate = (String) request.getAdditionalData().getOrDefault("startDate", "");

            document.add(new Paragraph("Je soussigné(e), Directeur des Ressources Humaines de la société ZENTRA,")
                    .setFontSize(11)
                    .setTextAlignment(TextAlignment.JUSTIFIED));

            document.add(new Paragraph("\n"));

            // Encadré informations employé
            com.itextpdf.layout.element.Table employeeInfoTable = new com.itextpdf.layout.element.Table(1);
            employeeInfoTable.setWidth(com.itextpdf.layout.properties.UnitValue.createPercentValue(90));
            employeeInfoTable.setMarginLeft(30);

            com.itextpdf.layout.element.Cell employeeInfoCell = new com.itextpdf.layout.element.Cell();
            employeeInfoCell.setBackgroundColor(new com.itextpdf.kernel.colors.DeviceRgb(230, 250, 230));
            employeeInfoCell.setPadding(15);

            employeeInfoCell.add(new Paragraph("ATTESTE QUE")
                    .setFontSize(12)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontColor(com.itextpdf.kernel.colors.ColorConstants.GREEN));

            employeeInfoCell.add(new Paragraph("\n"));

            employeeInfoCell.add(new Paragraph("Monsieur / Madame : " + employeeName)
                    .setFontSize(11)
                    .setBold());

            if (!position.isEmpty()) {
                employeeInfoCell.add(new Paragraph("Fonction : " + position)
                        .setFontSize(10));
            }

            if (!startDate.isEmpty()) {
                employeeInfoCell.add(new Paragraph("Employé(e) depuis le : " + startDate)
                        .setFontSize(10));
            }

            employeeInfoTable.addCell(employeeInfoCell);
            document.add(employeeInfoTable);

            document.add(new Paragraph("\n"));

            document.add(new Paragraph("est effectivement employé(e) au sein de notre entreprise et exerce ses " +
                    "fonctions avec compétence et assiduité.")
                    .setFontSize(11)
                    .setTextAlignment(TextAlignment.JUSTIFIED));

            document.add(new Paragraph("\n"));

            document.add(new Paragraph("Durant l'exercice de ses fonctions, Monsieur/Madame " + employeeName +
                    " a fait preuve de professionnalisme et s'est acquitté(e) de ses missions avec sérieux et efficacité.")
                    .setFontSize(11)
                    .setTextAlignment(TextAlignment.JUSTIFIED));

            document.add(new Paragraph("\n"));

            document.add(new Paragraph("Cette attestation est délivrée à l'intéressé(e) pour servir et valoir ce que de droit.")
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.JUSTIFIED)
                    .setItalic());

            document.add(new Paragraph("\n\n"));

            // Pied de page avec date et signature
            document.add(new Paragraph("Fait à Antananarivo, le " +
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy", java.util.Locale.FRENCH)))
                    .setFontSize(10));

            document.add(new Paragraph("\n\n"));

            // Zone signature avec encadré
            com.itextpdf.layout.element.Table signatureTable = new com.itextpdf.layout.element.Table(1);
            signatureTable.setWidth(com.itextpdf.layout.properties.UnitValue.createPercentValue(40));
            signatureTable.setMarginLeft(350);

            com.itextpdf.layout.element.Cell signatureCell = new com.itextpdf.layout.element.Cell();
            signatureCell.setBorder(new com.itextpdf.layout.borders.SolidBorder(
                com.itextpdf.kernel.colors.ColorConstants.GRAY, 1));
            signatureCell.setPadding(15);
            signatureCell.setHeight(80);

            signatureCell.add(new Paragraph("Le Directeur des RH")
                    .setFontSize(10)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER));
            signatureCell.add(new Paragraph("\n\n"));
            signatureCell.add(new Paragraph("Signature et cachet")
                    .setFontSize(9)
                    .setItalic()
                    .setTextAlignment(TextAlignment.CENTER));

            signatureTable.addCell(signatureCell);
            document.add(signatureTable);

            document.add(new Paragraph("\n"));

            // Note de bas de page
            document.add(new Paragraph("________________________________________")
                    .setFontSize(8)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontColor(com.itextpdf.kernel.colors.ColorConstants.GRAY));
            document.add(new Paragraph("Document confidentiel - À usage unique - Ne pas reproduire")
                    .setFontSize(7)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setItalic()
                    .setFontColor(com.itextpdf.kernel.colors.ColorConstants.GRAY));

        } finally {
            document.close();
        }
    }

    private void generateCertificate(String filePath, DocumentGenerationRequest request) throws Exception {
        PdfWriter writer = new PdfWriter(new FileOutputStream(filePath));
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        try {
            // En-tête avec fond orange
            com.itextpdf.layout.element.Table headerTable = new com.itextpdf.layout.element.Table(1);
            headerTable.setWidth(com.itextpdf.layout.properties.UnitValue.createPercentValue(100));

            com.itextpdf.layout.element.Cell headerCell = new com.itextpdf.layout.element.Cell();
            headerCell.setBackgroundColor(com.itextpdf.kernel.colors.ColorConstants.ORANGE);
            headerCell.setPadding(15);
            headerCell.add(new Paragraph("CERTIFICAT DE TRAVAIL")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(20)
                    .setBold()
                    .setFontColor(com.itextpdf.kernel.colors.ColorConstants.WHITE));
            headerTable.addCell(headerCell);
            document.add(headerTable);

            document.add(new Paragraph("\n"));

            // Informations entreprise
            com.itextpdf.layout.element.Table companyTable = new com.itextpdf.layout.element.Table(1);
            companyTable.setWidth(com.itextpdf.layout.properties.UnitValue.createPercentValue(100));

            com.itextpdf.layout.element.Cell companyCell = new com.itextpdf.layout.element.Cell();
            companyCell.setBorder(new com.itextpdf.layout.borders.SolidBorder(
                com.itextpdf.kernel.colors.ColorConstants.ORANGE, 2));
            companyCell.setPadding(10);

            companyCell.add(new Paragraph("ZENTRA")
                    .setFontSize(14)
                    .setBold()
                    .setFontColor(com.itextpdf.kernel.colors.ColorConstants.ORANGE));
            companyCell.add(new Paragraph("Solutions RH & Gestion d'Entreprise")
                    .setFontSize(10)
                    .setItalic());
            companyCell.add(new Paragraph("Lot II M 34 Ampefiloha, Antananarivo 101, Madagascar")
                    .setFontSize(9));
            companyCell.add(new Paragraph("SIRET: 123 456 789 00012")
                    .setFontSize(9));

            companyTable.addCell(companyCell);
            document.add(companyTable);

            document.add(new Paragraph("\n\n"));

            // Préambule
            document.add(new Paragraph("Je soussigné(e), agissant en qualité de Directeur des Ressources Humaines " +
                    "de la société ZENTRA, dûment habilité(e) à cet effet,")
                    .setFontSize(11)
                    .setTextAlignment(TextAlignment.JUSTIFIED));

            document.add(new Paragraph("\n"));

            // Informations employé
            String employeeName = getEmployeeName(request);
            String position = (String) request.getAdditionalData().getOrDefault("position", "");
            String startDate = (String) request.getAdditionalData().getOrDefault("startDate", "");
            String endDate = (String) request.getAdditionalData().getOrDefault("endDate",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

            document.add(new Paragraph("CERTIFIE")
                    .setFontSize(14)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontColor(com.itextpdf.kernel.colors.ColorConstants.ORANGE));

            document.add(new Paragraph("\n"));

            // Tableau informations
            com.itextpdf.layout.element.Table infoTable = new com.itextpdf.layout.element.Table(new float[]{2, 3});
            infoTable.setWidth(com.itextpdf.layout.properties.UnitValue.createPercentValue(90));
            infoTable.setMarginLeft(30);

            infoTable.addCell(createTableCell("Nom et Prénom:", true));
            infoTable.addCell(createTableCell(employeeName, false));

            if (!position.isEmpty()) {
                infoTable.addCell(createTableCell("Fonction occupée:", true));
                infoTable.addCell(createTableCell(position, false));
            }

            if (!startDate.isEmpty()) {
                infoTable.addCell(createTableCell("Période d'emploi:", true));
                infoTable.addCell(createTableCell("Du " + startDate + " au " + endDate, false));
            }

            document.add(infoTable);

            document.add(new Paragraph("\n"));

            // Corps du certificat
            document.add(new Paragraph("a été employé(e) au sein de notre entreprise durant la période susmentionnée.")
                    .setFontSize(11)
                    .setTextAlignment(TextAlignment.JUSTIFIED));

            document.add(new Paragraph("\n"));

            document.add(new Paragraph("Durant son emploi, Monsieur/Madame " + employeeName +
                    " a exercé ses fonctions avec professionnalisme et a su démontrer ses compétences " +
                    "et son engagement envers les objectifs de l'entreprise.")
                    .setFontSize(11)
                    .setTextAlignment(TextAlignment.JUSTIFIED));

            document.add(new Paragraph("\n"));

            document.add(new Paragraph("Le contrat de travail a pris fin le " + endDate +
                    " dans le cadre d'une rupture conventionnelle / fin de contrat.")
                    .setFontSize(11)
                    .setTextAlignment(TextAlignment.JUSTIFIED));

            document.add(new Paragraph("\n"));

            document.add(new Paragraph("À la date de ce jour, l'intéressé(e) est libre de tout engagement vis-à-vis de notre société.")
                    .setFontSize(11)
                    .setTextAlignment(TextAlignment.JUSTIFIED)
                    .setBold());

            document.add(new Paragraph("\n"));

            document.add(new Paragraph("Le présent certificat est remis à l'intéressé(e) pour servir et valoir ce que de droit.")
                    .setFontSize(10)
                    .setItalic());

            document.add(new Paragraph("\n\n"));

            // En pied de faire à
            document.add(new Paragraph("Fait à Antananarivo, le " +
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy", java.util.Locale.FRENCH)))
                    .setFontSize(10));

            document.add(new Paragraph("\n\n"));

            // Signature
            com.itextpdf.layout.element.Table signatureTable = new com.itextpdf.layout.element.Table(1);
            signatureTable.setWidth(com.itextpdf.layout.properties.UnitValue.createPercentValue(40));
            signatureTable.setMarginLeft(350);

            com.itextpdf.layout.element.Cell signatureCell = new com.itextpdf.layout.element.Cell();
            signatureCell.setBorder(new com.itextpdf.layout.borders.SolidBorder(
                com.itextpdf.kernel.colors.ColorConstants.ORANGE, 1));
            signatureCell.setPadding(15);
            signatureCell.setHeight(80);

            signatureCell.add(new Paragraph("Le Directeur des RH")
                    .setFontSize(10)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER));
            signatureCell.add(new Paragraph("\n\n"));
            signatureCell.add(new Paragraph("Signature et cachet")
                    .setFontSize(9)
                    .setItalic()
                    .setTextAlignment(TextAlignment.CENTER));

            signatureTable.addCell(signatureCell);
            document.add(signatureTable);

            document.add(new Paragraph("\n"));

            // Note légale
            com.itextpdf.layout.element.Table legalTable = new com.itextpdf.layout.element.Table(1);
            legalTable.setWidth(com.itextpdf.layout.properties.UnitValue.createPercentValue(100));

            com.itextpdf.layout.element.Cell legalCell = new com.itextpdf.layout.element.Cell();
            legalCell.setBackgroundColor(new com.itextpdf.kernel.colors.DeviceRgb(255, 245, 230));
            legalCell.setPadding(8);
            legalCell.add(new Paragraph("Conformément aux dispositions du Code du Travail, le présent certificat " +
                    "est établi en un seul exemplaire et remis au salarié.")
                    .setFontSize(8)
                    .setItalic()
                    .setTextAlignment(TextAlignment.JUSTIFIED));

            legalTable.addCell(legalCell);
            document.add(legalTable);

        } finally {
            document.close();
        }
    }

    private void generatePayslip(String filePath, DocumentGenerationRequest request) throws Exception {
        PdfWriter writer = new PdfWriter(new FileOutputStream(filePath));
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        try {
            // En-tête avec fond bleu foncé
            com.itextpdf.layout.element.Table headerTable = new com.itextpdf.layout.element.Table(1);
            headerTable.setWidth(com.itextpdf.layout.properties.UnitValue.createPercentValue(100));

            com.itextpdf.layout.element.Cell headerCell = new com.itextpdf.layout.element.Cell();
            headerCell.setBackgroundColor(new com.itextpdf.kernel.colors.DeviceRgb(0, 51, 102));
            headerCell.setPadding(15);
            headerCell.add(new Paragraph("BULLETIN DE PAIE")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(20)
                    .setBold()
                    .setFontColor(com.itextpdf.kernel.colors.ColorConstants.WHITE));
            headerTable.addCell(headerCell);
            document.add(headerTable);

            document.add(new Paragraph("\n"));

            // Période et numéro
            String period = (String) request.getAdditionalData().getOrDefault("period",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMMM yyyy", java.util.Locale.FRENCH)));
            String bulletinNumber = "BP-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMM")) +
                "-" + String.format("%04d", request.getEmployeeId());

            com.itextpdf.layout.element.Table periodTable = new com.itextpdf.layout.element.Table(2);
            periodTable.setWidth(com.itextpdf.layout.properties.UnitValue.createPercentValue(100));

            periodTable.addCell(createTableCell("Période: " + period, true));
            periodTable.addCell(createTableCell("N° Bulletin: " + bulletinNumber, true)
                .setTextAlignment(TextAlignment.RIGHT));

            document.add(periodTable);

            document.add(new Paragraph("\n"));

            // Informations Entreprise et Employé
            com.itextpdf.layout.element.Table infoTable = new com.itextpdf.layout.element.Table(2);
            infoTable.setWidth(com.itextpdf.layout.properties.UnitValue.createPercentValue(100));

            // Colonne Entreprise
            com.itextpdf.layout.element.Cell employerCell = new com.itextpdf.layout.element.Cell();
            employerCell.setBorder(new com.itextpdf.layout.borders.SolidBorder(
                com.itextpdf.kernel.colors.ColorConstants.GRAY, 1));
            employerCell.setPadding(10);
            employerCell.setBackgroundColor(new com.itextpdf.kernel.colors.DeviceRgb(240, 248, 255));

            employerCell.add(new Paragraph("EMPLOYEUR")
                    .setFontSize(10)
                    .setBold()
                    .setFontColor(new com.itextpdf.kernel.colors.DeviceRgb(0, 51, 102)));
            employerCell.add(new Paragraph("ZENTRA")
                    .setFontSize(11)
                    .setBold());
            employerCell.add(new Paragraph("Solutions RH & Gestion")
                    .setFontSize(9));
            employerCell.add(new Paragraph("Antananarivo, Madagascar")
                    .setFontSize(9));
            employerCell.add(new Paragraph("SIRET: 123 456 789 00012")
                    .setFontSize(8));
            employerCell.add(new Paragraph("Code NAF: 6202A")
                    .setFontSize(8));

            // Colonne Employé
            String employeeName = getEmployeeName(request);
            String position = (String) request.getAdditionalData().getOrDefault("position", "");

            com.itextpdf.layout.element.Cell employeeCell = new com.itextpdf.layout.element.Cell();
            employeeCell.setBorder(new com.itextpdf.layout.borders.SolidBorder(
                com.itextpdf.kernel.colors.ColorConstants.GRAY, 1));
            employeeCell.setPadding(10);
            employeeCell.setBackgroundColor(new com.itextpdf.kernel.colors.DeviceRgb(240, 255, 240));

            employeeCell.add(new Paragraph("SALARIÉ(E)")
                    .setFontSize(10)
                    .setBold()
                    .setFontColor(new com.itextpdf.kernel.colors.DeviceRgb(0, 102, 51)));
            employeeCell.add(new Paragraph(employeeName)
                    .setFontSize(11)
                    .setBold());
            if (!position.isEmpty()) {
                employeeCell.add(new Paragraph("Fonction: " + position)
                        .setFontSize(9));
            }
            employeeCell.add(new Paragraph("Matricule: EMP-" + request.getEmployeeId())
                    .setFontSize(8));
            employeeCell.add(new Paragraph("N° Sécurité Sociale: XXX XXX XXX")
                    .setFontSize(8));

            infoTable.addCell(employerCell);
            infoTable.addCell(employeeCell);
            document.add(infoTable);

            document.add(new Paragraph("\n"));

            // Tableau détaillé des rémunérations
            double grossSalary = Double.parseDouble(
                    request.getAdditionalData().getOrDefault("grossSalary", "0").toString());

            com.itextpdf.layout.element.Table salaryTable = new com.itextpdf.layout.element.Table(new float[]{4, 1, 2, 2});
            salaryTable.setWidth(com.itextpdf.layout.properties.UnitValue.createPercentValue(100));

            // En-tête du tableau
            com.itextpdf.layout.element.Cell[] headerCells = {
                createTableCell("LIBELLÉ", true),
                createTableCell("BASE", true),
                createTableCell("TAUX", true),
                createTableCell("MONTANT", true)
            };

            for (com.itextpdf.layout.element.Cell cell : headerCells) {
                cell.setBackgroundColor(new com.itextpdf.kernel.colors.DeviceRgb(0, 51, 102));
                cell.setFontColor(com.itextpdf.kernel.colors.ColorConstants.WHITE);
                cell.setPadding(8);
                cell.setTextAlignment(TextAlignment.CENTER);
                salaryTable.addCell(cell);
            }

            // Salaire de base
            salaryTable.addCell(createTableCell("Salaire de base", false));
            salaryTable.addCell(createTableCell("1", false).setTextAlignment(TextAlignment.CENTER));
            salaryTable.addCell(createTableCell("-", false).setTextAlignment(TextAlignment.CENTER));
            salaryTable.addCell(createTableCell(String.format("%,.0f Ar", grossSalary), false)
                    .setTextAlignment(TextAlignment.RIGHT));

            // Prime éventuelle (exemple)
            double prime = grossSalary * 0.05;
            salaryTable.addCell(createTableCell("Prime d'ancienneté", false));
            salaryTable.addCell(createTableCell("1", false).setTextAlignment(TextAlignment.CENTER));
            salaryTable.addCell(createTableCell("5%", false).setTextAlignment(TextAlignment.CENTER));
            salaryTable.addCell(createTableCell(String.format("%,.0f Ar", prime), false)
                    .setTextAlignment(TextAlignment.RIGHT));

            // Total brut
            double totalGross = grossSalary + prime;
            com.itextpdf.layout.element.Cell totalGrossCell = new com.itextpdf.layout.element.Cell(1, 3);
            totalGrossCell.add(new Paragraph("TOTAL BRUT")
                    .setFontSize(10)
                    .setBold());
            totalGrossCell.setPadding(5);
            totalGrossCell.setBackgroundColor(new com.itextpdf.kernel.colors.DeviceRgb(220, 220, 220));
            salaryTable.addCell(totalGrossCell);

            com.itextpdf.layout.element.Cell totalGrossAmountCell = createTableCell(
                    String.format("%,.0f Ar", totalGross), true);
            totalGrossAmountCell.setTextAlignment(TextAlignment.RIGHT);
            totalGrossAmountCell.setBackgroundColor(new com.itextpdf.kernel.colors.DeviceRgb(220, 220, 220));
            salaryTable.addCell(totalGrossAmountCell);

            // COTISATIONS SALARIALES
            com.itextpdf.layout.element.Cell cotisationsHeader = new com.itextpdf.layout.element.Cell(1, 4);
            cotisationsHeader.add(new Paragraph("COTISATIONS SALARIALES")
                    .setFontSize(10)
                    .setBold()
                    .setFontColor(com.itextpdf.kernel.colors.ColorConstants.RED));
            cotisationsHeader.setPadding(5);
            cotisationsHeader.setBackgroundColor(new com.itextpdf.kernel.colors.DeviceRgb(255, 240, 240));
            salaryTable.addCell(cotisationsHeader);

            // CNaPS (Caisse Nationale de Prévoyance Sociale)
            double cnaps = totalGross * 0.01; // 1% employé
            salaryTable.addCell(createTableCell("CNaPS (Retraite)", false));
            salaryTable.addCell(createTableCell(String.format("%.0f", totalGross), false)
                    .setTextAlignment(TextAlignment.CENTER));
            salaryTable.addCell(createTableCell("1%", false).setTextAlignment(TextAlignment.CENTER));
            salaryTable.addCell(createTableCell(String.format("-%,.0f Ar", cnaps), false)
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setFontColor(com.itextpdf.kernel.colors.ColorConstants.RED));

            // OSTIE (Organisme Sanitaire Tananarivien Inter-Entreprises)
            double ostie = totalGross * 0.01; // 1% employé
            salaryTable.addCell(createTableCell("OSTIE (Santé)", false));
            salaryTable.addCell(createTableCell(String.format("%.0f", totalGross), false)
                    .setTextAlignment(TextAlignment.CENTER));
            salaryTable.addCell(createTableCell("1%", false).setTextAlignment(TextAlignment.CENTER));
            salaryTable.addCell(createTableCell(String.format("-%,.0f Ar", ostie), false)
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setFontColor(com.itextpdf.kernel.colors.ColorConstants.RED));

            // IRSA (Impôt sur les Revenus Salariaux et Assimilés)
            double irsa = calculateIRSA(totalGross);
            salaryTable.addCell(createTableCell("IRSA (Impôts)", false));
            salaryTable.addCell(createTableCell(String.format("%.0f", totalGross), false)
                    .setTextAlignment(TextAlignment.CENTER));
            salaryTable.addCell(createTableCell("Variable", false).setTextAlignment(TextAlignment.CENTER));
            salaryTable.addCell(createTableCell(String.format("-%,.0f Ar", irsa), false)
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setFontColor(com.itextpdf.kernel.colors.ColorConstants.RED));

            // Total cotisations
            double totalCotisations = cnaps + ostie + irsa;
            com.itextpdf.layout.element.Cell totalCotisationsCell = new com.itextpdf.layout.element.Cell(1, 3);
            totalCotisationsCell.add(new Paragraph("TOTAL RETENUES")
                    .setFontSize(10)
                    .setBold()
                    .setFontColor(com.itextpdf.kernel.colors.ColorConstants.RED));
            totalCotisationsCell.setPadding(5);
            totalCotisationsCell.setBackgroundColor(new com.itextpdf.kernel.colors.DeviceRgb(255, 230, 230));
            salaryTable.addCell(totalCotisationsCell);

            com.itextpdf.layout.element.Cell totalCotisationsAmountCell = createTableCell(
                    String.format("-%,.0f Ar", totalCotisations), true);
            totalCotisationsAmountCell.setTextAlignment(TextAlignment.RIGHT);
            totalCotisationsAmountCell.setBackgroundColor(new com.itextpdf.kernel.colors.DeviceRgb(255, 230, 230));
            totalCotisationsAmountCell.setFontColor(com.itextpdf.kernel.colors.ColorConstants.RED);
            salaryTable.addCell(totalCotisationsAmountCell);

            // NET À PAYER
            double netSalary = totalGross - totalCotisations;
            com.itextpdf.layout.element.Cell netLabelCell = new com.itextpdf.layout.element.Cell(1, 3);
            netLabelCell.add(new Paragraph("NET À PAYER")
                    .setFontSize(12)
                    .setBold()
                    .setFontColor(com.itextpdf.kernel.colors.ColorConstants.WHITE));
            netLabelCell.setPadding(8);
            netLabelCell.setBackgroundColor(new com.itextpdf.kernel.colors.DeviceRgb(0, 102, 51));
            salaryTable.addCell(netLabelCell);

            com.itextpdf.layout.element.Cell netAmountCell = createTableCell(
                    String.format("%,.0f Ar", netSalary), true);
            netAmountCell.setTextAlignment(TextAlignment.RIGHT);
            netAmountCell.setBackgroundColor(new com.itextpdf.kernel.colors.DeviceRgb(0, 102, 51));
            netAmountCell.setFontColor(com.itextpdf.kernel.colors.ColorConstants.WHITE);
            netAmountCell.setFontSize(12);
            salaryTable.addCell(netAmountCell);

            document.add(salaryTable);

            document.add(new Paragraph("\n"));

            // Récapitulatif
            com.itextpdf.layout.element.Table summaryTable = new com.itextpdf.layout.element.Table(2);
            summaryTable.setWidth(com.itextpdf.layout.properties.UnitValue.createPercentValue(50));
            summaryTable.setMarginLeft(250);

            summaryTable.addCell(createTableCell("Net imposable:", true));
            summaryTable.addCell(createTableCell(String.format("%,.0f Ar", totalGross - cnaps - ostie), false)
                    .setTextAlignment(TextAlignment.RIGHT));

            summaryTable.addCell(createTableCell("Net à payer:", true)
                    .setBackgroundColor(new com.itextpdf.kernel.colors.DeviceRgb(200, 255, 200)));
            summaryTable.addCell(createTableCell(String.format("%,.0f Ar", netSalary), true)
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setBackgroundColor(new com.itextpdf.kernel.colors.DeviceRgb(200, 255, 200)));

            document.add(summaryTable);

            document.add(new Paragraph("\n"));

            // Informations de paiement
            com.itextpdf.layout.element.Table paymentTable = new com.itextpdf.layout.element.Table(1);
            paymentTable.setWidth(com.itextpdf.layout.properties.UnitValue.createPercentValue(100));

            com.itextpdf.layout.element.Cell paymentCell = new com.itextpdf.layout.element.Cell();
            paymentCell.setBackgroundColor(new com.itextpdf.kernel.colors.DeviceRgb(255, 255, 230));
            paymentCell.setPadding(10);
            paymentCell.add(new Paragraph("MODE DE PAIEMENT")
                    .setFontSize(10)
                    .setBold());
            paymentCell.add(new Paragraph("Virement bancaire - Date de paiement: " +
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                    .setFontSize(9));

            paymentTable.addCell(paymentCell);
            document.add(paymentTable);

            document.add(new Paragraph("\n"));

            // Mentions légales
            document.add(new Paragraph("Document confidentiel à conserver - Ne constitue pas un titre de créance")
                    .setFontSize(7)
                    .setItalic()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontColor(com.itextpdf.kernel.colors.ColorConstants.GRAY));

        } finally {
            document.close();
        }
    }

    private double calculateIRSA(double grossSalary) {
        // Calcul simplifié de l'IRSA Madagascar (barème progressif)
        double taxableIncome = grossSalary - (grossSalary * 0.02); // 2% déduction

        if (taxableIncome <= 350000) {
            return 0;
        } else if (taxableIncome <= 400000) {
            return (taxableIncome - 350000) * 0.05;
        } else if (taxableIncome <= 500000) {
            return 2500 + (taxableIncome - 400000) * 0.10;
        } else if (taxableIncome <= 600000) {
            return 12500 + (taxableIncome - 500000) * 0.15;
        } else {
            return 27500 + (taxableIncome - 600000) * 0.20;
        }
    }

    private String generateFileName(String documentType, Integer employeeId) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        return String.format("%s_%d_%s_%s.pdf",
                documentType,
                employeeId,
                timestamp,
                UUID.randomUUID().toString().substring(0, 8));
    }

    private String getEmployeeName(DocumentGenerationRequest request) {
        // In a real implementation, this would fetch from employee service/repository
        return (String) request.getAdditionalData().getOrDefault("employeeName", "Nom de l'employé");
    }

    public List<GeneratedDocumentDTO> getEmployeeDocuments(Integer employeeId) {
        return documentRepository.findByEmployeeId(employeeId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<GeneratedDocumentDTO> getAllDocuments() {
        return documentRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private GeneratedDocumentDTO convertToDTO(GeneratedDocument document) {
        GeneratedDocumentDTO dto = new GeneratedDocumentDTO();
        dto.setId(document.getId());
        dto.setDocumentType(document.getDocumentType());
        dto.setEmployeeId(document.getEmployeeId());
        dto.setEmployeeName(document.getEmployeeName());
        dto.setFilePath(document.getFilePath());
        dto.setFileName(document.getFileName());
        dto.setGeneratedAt(document.getGeneratedAt());
        dto.setGeneratedBy(document.getGeneratedBy());
        return dto;
    }
}

