package in.harsh.moneymanager.repositry;

import in.harsh.moneymanager.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepositry extends JpaRepository {

    Optional<ProfileEntity> findByEmail(String email);
}
