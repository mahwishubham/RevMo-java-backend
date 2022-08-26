package com.revature.dao;

import com.revature.model.User;
import com.revature.utility.ConnectionUtility;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;


public class UserDao {

    public User getUserByEmailAndPassword(String email, String password) throws SQLException {
        try (Connection con = ConnectionUtility.createConnection()) {
            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM users WHERE email=? AND pass=convert_to(?, 'LATIN1')");

            pstmt.setString(1, email);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new User(rs.getInt("id"), rs.getString("first_name"),
                        rs.getString("last_name"), rs.getString("email"),
                        rs.getString("pass"), rs.getString("phone"),
                        rs.getString("role_id"));
            } else {
                return null;
            }
        }
    }
<<<<<<< Updated upstream
=======

    public User getUserByEmail(String email) {
        try (Connection con = ConnectionUtility.createConnection()) {
            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM users WHERE email=?");

            pstmt.setString(1, email);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new User(rs.getInt("id"), rs.getString("first_name"),
                        rs.getString("last_name"), rs.getString("email"),
                        rs.getString("pass"), rs.getString("phone"),
                        rs.getString("role_id"));
            }

            return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public String updateEmail(int userId, String email) {
        try (Connection con = ConnectionUtility.createConnection()) {
            PreparedStatement pstmt = con.prepareStatement("UPDATE users SET email=? WHERE id=? RETURNING *");
            pstmt.setString(1, email);
            pstmt.setInt(2, userId);
            ResultSet rs = pstmt.executeQuery();

            return "Email updated.";

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String updatephone(int userId, String phoneNumber) {
        try (Connection con = ConnectionUtility.createConnection()) {
            PreparedStatement pstmt = con.prepareStatement("UPDATE users SET phone=? WHERE id=? RETURNING *");
            pstmt.setString(1, phoneNumber);
            pstmt.setInt(2, userId);
            ResultSet rs = pstmt.executeQuery();

            return "phone updated. Congrats?";

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public String updateFirstName(int userId, String firstName) {
        try (Connection con = ConnectionUtility.createConnection()) {
            PreparedStatement pstmt = con.prepareStatement("UPDATE users SET first_name=? WHERE id=? RETURNING *");
            pstmt.setString(1, firstName);
            pstmt.setInt(2, userId);
            ResultSet rs = pstmt.executeQuery();

            return "First name updated. Congrats!";

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public String updateLastName(int userId, String lastName) {
        try (Connection con = ConnectionUtility.createConnection()) {
            PreparedStatement pstmt = con.prepareStatement("UPDATE users SET last_name=? WHERE id=? RETURNING *");
            pstmt.setString(1, lastName);
            pstmt.setInt(2, userId);
            ResultSet rs = pstmt.executeQuery();

            return "Last name updated. Congrats?";

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getRequesteeEmailByTransactionId(int transactionId) {
        try (Connection con = ConnectionUtility.createConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT u.email FROM users u " +
                    "JOIN users_with_accounts uwa ON uwa.user_id = u.id " +
                    "JOIN transactions t ON uwa.account_id = t.sending_id " +
                    "WHERE t.id = ?");
            ps.setInt(1, transactionId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("email");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<String> getReceiverEmailByTransactionId(int transactionId) {
        try (Connection con = ConnectionUtility.createConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT u.email FROM users u " +
                    "JOIN users_with_accounts uwa ON uwa.user_id = u.id " +
                    "JOIN transactions t ON uwa.account_id = t.receiving_id " +
                    "WHERE t.id = ?");
            ps.setInt(1, transactionId);
            ResultSet rs = ps.executeQuery();
            List<String> emails = new ArrayList<>();
            while (rs.next()) {
                emails.add(rs.getString("email"));
            }
            return emails;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
>>>>>>> Stashed changes
}
