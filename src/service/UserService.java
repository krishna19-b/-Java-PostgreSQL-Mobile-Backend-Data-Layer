package service;

import dao.UserDao;
import model.User;

public class UserService {

    private final UserDao userDAO;

    public UserService() {

        this.userDAO = new UserDao();
    }

    public int register(User user) {

        if (user.getName() == null ||
                user.getName().isBlank()) {

            throw new IllegalArgumentException(
                    "Name cannot be empty"
            );
        }

        if (user.getEmail() == null ||
                user.getEmail().isBlank()) {

            throw new IllegalArgumentException(
                    "Email cannot be empty"
            );
        }

        if (user.getPassword() == null ||
                user.getPassword().isBlank()) {

            throw new IllegalArgumentException(
                    "Password cannot be empty"
            );
        }

        return userDAO.save(user);
    }

    public User login(String email,
                      String password) {

        User user =
                userDAO.findByEmail(email);

        if (user == null) {

            return null;
        }

        if (!user.getPassword()
                .equals(password)) {

            return null;
        }

        return user;
    }
}