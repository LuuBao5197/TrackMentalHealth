package fpt.aptech.trackmentalhealth.repository.contentcreator;

import fpt.aptech.trackmentalhealth.entities.ContentCreator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentCreatorRepository extends JpaRepository<ContentCreator, Integer> {
    // Bạn có thể thêm các method tuỳ chỉnh nếu cần
}
