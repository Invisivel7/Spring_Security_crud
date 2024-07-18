package security.security.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import security.security.entities.Tweet;

public interface TweetRepository extends JpaRepository<Tweet, Long> {

}
