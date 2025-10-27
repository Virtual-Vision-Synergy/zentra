package org.pentagone.business.zentracore.hr.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/files")
@CrossOrigin(origins = "*")
public class FileController {

    @Value("${upload-dir.candidates.cvs:uploads/candidates/cvs}")
    private String cvUploadDir;

    @Value("${upload-dir.candidates.motivation-letters:uploads/candidates/motivation-letters}")
    private String motivationLetterUploadDir;

    /**
     * Télécharge un fichier en utilisant le chemin complet depuis la base de données
     * Le chemin peut être:
     * - files/cv/filename.pdf (ancien format)
     * - uploads/candidates/cvs/filename.pdf (nouveau format)
     * - ou un chemin absolu
     */
    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam("path") String filePath) {
        try {
            System.out.println("Tentative de téléchargement du fichier: " + filePath);

            Path file = null;

            // Essayer différents chemins possibles
            // 1. Essayer le chemin tel quel (peut être un chemin absolu)
            Path directPath = Paths.get(filePath);
            if (Files.exists(directPath) && Files.isReadable(directPath)) {
                file = directPath;
                System.out.println("Fichier trouvé avec chemin direct: " + directPath);
            }

            // 2. Si le chemin commence par "files/cv/" ou "files/lettre/",
            //    mapper vers les nouveaux répertoires
            if (file == null && filePath.startsWith("files/cv/")) {
                String fileName = filePath.substring("files/cv/".length());
                Path cvPath = Paths.get(cvUploadDir, fileName);
                if (Files.exists(cvPath) && Files.isReadable(cvPath)) {
                    file = cvPath;
                    System.out.println("Fichier CV trouvé: " + cvPath);
                }
            }

            if (file == null && filePath.startsWith("files/lettre/")) {
                String fileName = filePath.substring("files/lettre/".length());
                Path letterPath = Paths.get(motivationLetterUploadDir, fileName);
                if (Files.exists(letterPath) && Files.isReadable(letterPath)) {
                    file = letterPath;
                    System.out.println("Fichier lettre trouvé: " + letterPath);
                }
            }

            // 3. Si le chemin commence déjà par uploads/, l'utiliser tel quel
            if (file == null && filePath.startsWith("uploads/")) {
                Path uploadsPath = Paths.get(filePath);
                if (Files.exists(uploadsPath) && Files.isReadable(uploadsPath)) {
                    file = uploadsPath;
                    System.out.println("Fichier uploads trouvé: " + uploadsPath);
                }
            }

            // 4. Essayer dans le répertoire CV par défaut
            if (file == null) {
                Path cvPath = Paths.get(cvUploadDir, filePath);
                if (Files.exists(cvPath) && Files.isReadable(cvPath)) {
                    file = cvPath;
                    System.out.println("Fichier trouvé dans répertoire CV: " + cvPath);
                }
            }

            // 5. Essayer dans le répertoire lettres de motivation
            if (file == null) {
                Path letterPath = Paths.get(motivationLetterUploadDir, filePath);
                if (Files.exists(letterPath) && Files.isReadable(letterPath)) {
                    file = letterPath;
                    System.out.println("Fichier trouvé dans répertoire lettres: " + letterPath);
                }
            }

            if (file == null || !Files.exists(file)) {
                System.err.println("Fichier non trouvé: " + filePath);
                System.err.println("Répertoire CV: " + cvUploadDir);
                System.err.println("Répertoire lettres: " + motivationLetterUploadDir);
                return ResponseEntity.notFound().build();
            }

            Resource resource = new UrlResource(file.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                System.err.println("Fichier non lisible: " + file);
                return ResponseEntity.notFound().build();
            }

            // Déterminer le type de contenu
            String contentType = Files.probeContentType(file);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            System.out.println("Envoi du fichier: " + file.getFileName() + " (type: " + contentType + ")");

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                           "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (MalformedURLException e) {
            System.err.println("Erreur URL malformée: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (IOException e) {
            System.err.println("Erreur I/O: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        } catch (Exception e) {
            System.err.println("Erreur inattendue: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Endpoint alternatif qui accepte simplement le nom du fichier
     * Cherche d'abord dans les CVs, puis dans les lettres de motivation
     */
    @GetMapping("/{filename}")
    public ResponseEntity<Resource> downloadFileByName(@PathVariable String filename) {
        try {
            System.out.println("Téléchargement par nom de fichier: " + filename);

            // Essayer d'abord dans le répertoire des CVs
            Path cvPath = Paths.get(cvUploadDir, filename);
            if (Files.exists(cvPath) && Files.isReadable(cvPath)) {
                return downloadFile(cvPath.toString());
            }

            // Puis dans le répertoire des lettres de motivation
            Path letterPath = Paths.get(motivationLetterUploadDir, filename);
            if (Files.exists(letterPath) && Files.isReadable(letterPath)) {
                return downloadFile(letterPath.toString());
            }

            System.err.println("Fichier non trouvé: " + filename);
            return ResponseEntity.notFound().build();

        } catch (Exception e) {
            System.err.println("Erreur: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}

