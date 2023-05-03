package com.project.tutorial.Controllers;

import com.itextpdf.text.DocumentException;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.project.tutorial.Models.User;
import com.project.tutorial.Models.UserFeed;
import com.project.tutorial.Repositories.UserFeedRepo;
import com.project.tutorial.Repositories.UserRepository;
import com.project.tutorial.Services.GeneratePdf;
import com.project.tutorial.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/feed")
public class UserFeedController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserFeedRepo userFeedRepo;
    @Autowired
    UserService userService;

    @GetMapping("/myfeed")
    public ResponseEntity<?> myFeed() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!userFeedRepo.findByEmail(email).isEmpty()) {
            return ResponseEntity.ok(userFeedRepo.findByEmail(email));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Feed is empty.");
    }

    @PostMapping("/newpost")
    public ResponseEntity<?> newpost(@RequestBody UserFeed userFeed) {
        if (userRepository.existsById(userFeed.getEmail())
                && userFeed.getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            userFeedRepo.save(userFeed);
            return ResponseEntity.ok("Feed uploaded.");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email not valid");
    }

    @GetMapping("/myfeed/{id}")
    public ResponseEntity<?> myFeedById(@PathVariable String id) {
        if (userFeedRepo.existsById(id)) {
            Optional<UserFeed>userOptional=userFeedRepo.findById(id);
            if(userOptional.isPresent()){
            return ResponseEntity.ok(userFeedRepo.findById(id).get());
            }
            return null;
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id not found.");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePost(@PathVariable String id) {
        String myEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        if (userFeedRepo.existsById(id)
                && userFeedRepo.findById(id).get().getEmail().equals(myEmail)) {
            Optional<UserFeed> userFeed = userFeedRepo.findById(id);
            userFeed.get().setAvailable(false);
            userFeedRepo.save(userFeed.get());
            return ResponseEntity.ok("Post has been Deleted , You can see it in Archive");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id not found.");
    }

    @DeleteMapping("/archive/{id}")
    public ResponseEntity<?> deleteArchive(@PathVariable String id){
        String myEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        if (userFeedRepo.existsById(id) && userFeedRepo.findById(id).get().getEmail().equals(myEmail)){
            Optional<UserFeed> userFeed = userFeedRepo.findById(id);
            if (!userFeed.get().isAvailable()){
                userFeedRepo.deleteById(id);
                userFeedRepo.save(userFeed.get());
                return ResponseEntity.ok("Content deleted in archive.");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Content not in false.");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User Id not valid.");
    }

    @GetMapping("/archive")
    public ResponseEntity<?> myArchive() {
        String myEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        List<UserFeed> myFeed = new ArrayList<>();
        List<UserFeed> allFeed = userFeedRepo.findAll();
        for (UserFeed i : allFeed) {
            if (i.getEmail().equals(myEmail) && !i.isAvailable()) {
                myFeed.add(i);
                }
            }
            if (myFeed.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Your archive is empty.");
            } else {
            return  ResponseEntity.ok(myFeed);
        }
    }
    @GetMapping("/archive/restore/{id}")
    public ResponseEntity<?> restoreFrmArchive(@PathVariable String id){
        String myEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        if (userFeedRepo.existsById(id) && userFeedRepo.findById(id).get().getEmail().equals(myEmail)){
            Optional<UserFeed> userFeed = userFeedRepo.findById(id);
            userFeed.get().setAvailable(true);
            userFeedRepo.save(userFeed.get());
            return ResponseEntity.ok("Restored content from archive.");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Content not found.");
    }
    @GetMapping("/view/friends")
    public List<List<UserFeed>> friendsFeed(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userRepository.findById(email);
        List<String> myFrdlist = user.get().getFollowing();
        return myFrdlist.stream().map((frd) -> userFeedRepo.findAllByEmailAndAvailableAndVisibility(frd,true,"friends")).collect(Collectors.toList());
    }
    @GetMapping("/view/public")
    public List<UserFeed> publicFeed(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userFeedRepo.findAllByVisibility("public");
    }
    @GetMapping("/view/private")
    private List<UserFeed> privateFeed(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userFeedRepo.findAllByEmailAndAvailableAndVisibility(email,true,"private");
    }
    @PostMapping("/csv/upload")
    private ResponseEntity<?> csvFileUpload(@RequestParam("file") MultipartFile file)throws IOException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (userRepository.findOneById(email).get().getEnabled()){
            InputStream inputStream = file.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            CsvToBean<UserFeed> csvToBean = new CsvToBeanBuilder<UserFeed>(reader)
                    .withType(UserFeed.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            List<UserFeed> users = csvToBean.parse();
            userFeedRepo.saveAll(users);
            return ResponseEntity.ok("File upload successfully.");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No access for this user.");
    }
    @GetMapping("/exportCsv")
    public void exportCsv(HttpServletResponse response) throws Exception{
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<UserFeed> myFeed = userFeedRepo.findByEmail(email);
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" + email + "\"");
        StatefulBeanToCsv<UserFeed> writer = new StatefulBeanToCsvBuilder<UserFeed>(response.getWriter())
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                .withOrderedResults(false)
                .build();
        writer.write(myFeed);
    }
    @GetMapping("/exportPdf")
    public ResponseEntity<InputStreamResource> exportPdf(HttpServletResponse response) throws DocumentException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        response.setContentType("application/pdf");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=\""+email+".Pdf\"";
        response.setHeader(headerKey,headerValue);

        List<UserFeed> userFeeds= userFeedRepo.findByEmail(email);
        ByteArrayInputStream bis = GeneratePdf.exportPdf(userFeeds);

        return ResponseEntity
                .ok()
                .header(headerKey,headerValue)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
}