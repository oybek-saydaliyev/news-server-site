package uz.news.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.news.base.ApiResponse;
import uz.news.dto.NewsDto;
import uz.news.dto.NewsPatchDto;
import uz.news.entity.enums.Status;
import uz.news.service.NewsService;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    //admins
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/news")
    public ResponseEntity<?> create(@RequestBody NewsDto dto){
        return ApiResponse.controller(newsService.create(dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/news/{id}/restore")
    public ResponseEntity<?> restore(@PathVariable Long id){
        return ApiResponse.controller(newsService.restore(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/news")
    public ResponseEntity<?> getAll(@RequestParam Status status,
                                    @RequestParam Long authorId,
                                    @RequestParam Long categoryId){
        return ApiResponse.controller(newsService.getAllAdmin(status, authorId, categoryId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/news/{id}")
    public ResponseEntity<?> get(@PathVariable Long id){
        return ApiResponse.controller(newsService.getOne(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/news/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody NewsDto dto){
        return ApiResponse.controller(newsService.update(id, dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/admin/news/{id}")
    public ResponseEntity<?> patch(@PathVariable Long id, @RequestBody NewsPatchDto dto){
        return ApiResponse.controller(newsService.updateContent(id, dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/admin/news/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestParam Status status){
        return ApiResponse.controller(newsService.updateStatus(id, status));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/news/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        return ApiResponse.controller(newsService.deleteSoft(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/news/{id}/hard")
    public ResponseEntity<?> hardDelete(@PathVariable Long id){
        return ApiResponse.controller(newsService.deleteHard(id));
    }

    //public
    @GetMapping("/public/news")
    public ResponseEntity<?> getAllPublic(@RequestParam(required = false) String search,
                                          @RequestParam(required = false) Long categoryId,
                                          @RequestParam(required = false) LocalDateTime from,
                                          @RequestParam(required = false) LocalDateTime to){
        return ApiResponse.controller(newsService.getAll(search, categoryId, from, to));
    }

    @GetMapping("/public/news/{id}")
    public ResponseEntity<?> getPublic(@PathVariable Long id){
        return ApiResponse.controller(newsService.getOnePublished(id));
    }

}
