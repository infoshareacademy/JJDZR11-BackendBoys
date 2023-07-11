package pl.isa.backendBoys.zgubaAppWeb.user;

import org.springframework.stereotype.Component;
import pl.isa.backendBoys.zgubaAppWeb.database.JsonService;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserDatabase {
    private final List<User> users = new ArrayList<>(JsonService.getUsersFromJsonFile());

    public List<User> getUsers() {
        return users;
    }

    public void add(User user) {
        users.add(user);
    }

    public void exortToJson() {
        JsonService.updateUsersJsonFile(users);
    }

    public void deleteUser(User loggedUser) {
        users.remove(loggedUser);
    }
}
