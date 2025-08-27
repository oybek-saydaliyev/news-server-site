package uz.news.repository;

import uz.news.base.BaseRepository;
import uz.news.entity.AuthUser;

import java.util.Optional;

public interface UserRepository extends BaseRepository<AuthUser> {
   Optional<AuthUser> findByUsername(String username);
}