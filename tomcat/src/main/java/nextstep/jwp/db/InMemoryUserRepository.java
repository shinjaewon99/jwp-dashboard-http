package nextstep.jwp.db;

import nextstep.jwp.model.User;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUserRepository {

    private static final Map<String, User> database = new ConcurrentHashMap<>();
    private static Long id;

    static {
        final User user = new User(1L, "shin", "pass", "qwer@naver.com");
        database.put(user.getAccount(), user);
    }

    public static void save(User user) {
        id += 1;
        final User saveUser = new User(id, user.getAccount(), user.getPassword(), user.getEmail());
        database.put(user.getAccount(), saveUser);
    }

    public static Optional<User> findByAccount(String account) {
        return Optional.ofNullable(database.get(account));
    }

    private InMemoryUserRepository() {
    }
}
