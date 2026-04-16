package com.imperiumfitness.DAO;

import javax.sql.DataSource;

import com.imperiumfitness.domain.Usuari;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class usuarisDAO {

    public static List<Usuari> getUsuaris(DataSource ds) {

        List<Usuari> usuaris = new ArrayList<>();

        String sql = "SELECT id, nom, email, contrasenya, rol FROM Usuari";

        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuari usuari = new Usuari(
                        rs.getLong("id"),
                        rs.getString("nom"),
                        rs.getString("email"),
                        rs.getString("contrasenya"),
                        rs.getString("rol")
                );

                usuaris.add(usuari);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return usuaris;
    }

    public static Usuari findByEmail(DataSource ds, String email) {

        String sql = "SELECT id, nom, email, contrasenya, rol FROM Usuari WHERE email = ?";

        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Usuari(
                            rs.getLong("id"),
                            rs.getString("nom"),
                            rs.getString("email"),
                            rs.getString("contrasenya"),
                            rs.getString("rol")
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}