package fpt.aptech.trackmentalhealth.api;

import fpt.aptech.trackmentalhealth.entities.Diary;
import fpt.aptech.trackmentalhealth.entities.Users;
import fpt.aptech.trackmentalhealth.repository.diary.DiaryRepository;
import fpt.aptech.trackmentalhealth.repository.user.UserRepository;
import fpt.aptech.trackmentalhealth.service.diary.DiaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/diaries")
@CrossOrigin(origins = "*")
public class DiaryController {

    private final DiaryService diaryService;
    private final UserRepository userRepository;
    private final DiaryRepository diaryRepository;

    public DiaryController(DiaryService diaryService, UserRepository userRepository, DiaryRepository diaryRepository) {
        this.diaryService = diaryService;
        this.userRepository = userRepository;
        this.diaryRepository = diaryRepository;
    }

    // Lấy tất cả nhật ký của user đang đăng nhập
    @GetMapping("/my")
    public ResponseEntity<List<Diary>> getMyDiaries() {
        Users user = getCurrentUser();
        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        List<Diary> diaries = diaryService.findByUserId(user.getId());
        return ResponseEntity.ok(diaries);
    }

    // Lấy nhật ký theo ID (nếu là của user)
    // ✅ API public - lấy nhật ký theo userId (không cần đăng nhập)
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Diary>> getDiariesByUserId(@PathVariable Integer userId) {
        Optional<Users> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<Diary> diaries = diaryService.findByUserId(userId);
        return ResponseEntity.ok(diaries);
    }
//    @PostMapping
//    public ResponseEntity<Diary> createDiary(@RequestBody Diary diary) {
//        if (diary.getUsers() == null || diary.getUsers().getId() == null) {
//            return ResponseEntity.badRequest().body(null);
//        }
//
//        Optional<Users> userOpt = userRepository.findById(diary.getUsers().getId());
//        if (userOpt.isEmpty()) {
//            return ResponseEntity.status(404).body(null);
//        }
//
//        diary.setUsers(userOpt.get());
//
//        if (diary.getDate() == null) {
//            diary.setDate(LocalDate.now());
//        }
//
//        Diary saved = diaryService.save(diary);
//        return ResponseEntity.ok(saved);
//    }



    // Tạo nhật ký mới (gán user đang đăng nhập)
    @PostMapping
    public ResponseEntity<Diary> createDiary(@RequestBody Diary diary) {
        Users user = getCurrentUser();
        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        diary.setUsers(user);
        if (diary.getDate() == null) {
            diary.setDate(LocalDate.now());
        }

        Diary saved = diaryService.save(diary);
        return ResponseEntity.ok(saved);
    }

//     Cập nhật nhật ký (chỉ nếu là của user)
    @PutMapping("/{id}")
    public ResponseEntity<Diary> updateDiary(@PathVariable Integer id, @RequestBody Diary diaryUpdate) {
        Users user = getCurrentUser();
        Optional<Diary> optionalDiary = diaryService.findById(id);

        if (optionalDiary.isEmpty() || !optionalDiary.get().getUsers().getId().equals(user.getId())) {
            return ResponseEntity.status(403).build();
        }

        Diary existing = optionalDiary.get();
        existing.setContent(diaryUpdate.getContent());
        existing.setDate(diaryUpdate.getDate());

        return ResponseEntity.ok(diaryService.save(existing));
    }
//    @PutMapping("/{id}")
//    public ResponseEntity<Diary> updateDiary(@PathVariable Integer id, @RequestBody Diary diaryUpdate) {
//        // ✅ THAY VÌ lấy user từ token, ta dùng luôn user được gửi trong diaryUpdate
//        Optional<Diary> optionalDiary = diaryService.findById(id);
//
//        if (optionalDiary.isEmpty() ||
//                !optionalDiary.get().getUsers().getId().equals(diaryUpdate.getUsers().getId())) {
//            return ResponseEntity.status(403).build();
//        }
//
//        Diary existing = optionalDiary.get();
//        existing.setContent(diaryUpdate.getContent());
//        existing.setDate(diaryUpdate.getDate());
//
//        return ResponseEntity.ok(diaryService.save(existing));
//    }

    // Xóa nhật ký (chỉ nếu là của user)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDiary(@PathVariable Integer id) {
        Users user = getCurrentUser();
        Optional<Diary> optionalDiary = diaryService.findById(id);

        if (optionalDiary.isEmpty() || !optionalDiary.get().getUsers().getId().equals(user.getId())) {
            return ResponseEntity.status(403).build();
        }

        diaryService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    // 👉 Hàm lấy người dùng hiện tại từ token
    private Users getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Email là username
        return userRepository.findAll().stream()
                .filter(u -> email.equals(u.getEmail()))
                .findFirst()
                .orElse(null);
    }
}
