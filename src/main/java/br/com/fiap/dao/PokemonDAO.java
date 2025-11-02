package br.com.fiap.dao;

import br.com.fiap.to.PokemonTO;

import java.sql.*;
import java.util.ArrayList;

public class PokemonDAO {
    public ArrayList<PokemonTO> findAll() {
        ArrayList<PokemonTO> pokemons = new ArrayList<PokemonTO>();
        String sql = "select * from ddd_pokemon order by codigo";
        PreparedStatement ps = null;

        Connection connection = ConnectionFactory.getConnection();

        if (connection == null) {
            System.out.println("Erro Crítico: Conexão com o banco de dados é nula. Verifique ConnectionFactory.");
            return pokemons;
        }

        try {
            ps = connection.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                PokemonTO pokemon = new PokemonTO();
                pokemon.setCodigo(rs.getLong("codigo"));
                pokemon.setNome(rs.getString("nome"));
                pokemon.setAltura(rs.getDouble("altura"));
                pokemon.setPeso(rs.getDouble("peso"));
                pokemon.setDataDeCaptura(rs.getDate("data_de_captura").toLocalDate());
                pokemon.setCategoria(rs.getString("categoria"));
                pokemons.add(pokemon);
            }
        } catch (SQLException e) {
            System.out.println("Erro na consulta SQL: " + e.getMessage());
        } finally {
            ConnectionFactory.closeConnection();
        }
        return pokemons;
    }

    public PokemonTO findByCodigo(Long codigo) {
        PokemonTO pokemon = new PokemonTO();
        Connection connection = ConnectionFactory.getConnection();
        if (connection == null) {
            System.out.println("Erro: Conexão nula em findByCodigo.");
            return null;
        }
        String sql = "select * from ddd_pokemon where codigo = ?";
        try(PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql))
        {
            ps.setLong(1, codigo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                pokemon.setCodigo(rs.getLong("codigo"));
                pokemon.setNome(rs.getString("nome"));
                pokemon.setAltura(rs.getDouble("altura"));
                pokemon.setPeso(rs.getDouble("peso"));
                pokemon.setDataDeCaptura(rs.getDate("data_de_captura").toLocalDate());
                pokemon.setCategoria(rs.getString("categoria"));
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Erro na consulta: " + e.getMessage());
        } finally {
            ConnectionFactory.closeConnection();
        }
        return pokemon;
    }

    public PokemonTO save(PokemonTO pokemon) {
        Connection connection = ConnectionFactory.getConnection();
        if (connection == null) {
            System.out.println("Erro: Conexão nula em update.");
            return null;
        }

        String sql = "update ddd_pokemon set nome=?, altura=?, peso=?, categoria=?, data_de_captura=? where codigo=?";

        try(PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setString(1, pokemon.getNome());
            ps.setDouble(2, pokemon.getAltura());
            ps.setDouble(3, pokemon.getPeso());
            ps.setString(4, pokemon.getCategoria());
            ps.setDate(5, Date.valueOf(pokemon.getDataDeCaptura()));
            ps.setLong(6, pokemon.getCodigo());

            if (ps.executeUpdate() > 0) {
                return pokemon;
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar: " + e.getMessage());
            return null;
        } finally {
            ConnectionFactory.closeConnection();
        }
    }

    public boolean delete(Long codigo) {
        String sql = "delete from ddd_pokemon where codigo = ?";
        try(PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql))
        {
            ps.setLong(1, codigo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao excluir: " + e.getMessage());
        } finally {
            ConnectionFactory.closeConnection();
        }
        return false;
    }

    public PokemonTO update(PokemonTO pokemon) {
        Connection connection = ConnectionFactory.getConnection();
        if (connection == null) {
            System.out.println("Erro: Conexão nula em findByCodigo.");
            return null;
        }
        String sql = "update ddd_pokemon set nome=?, altura=?, peso=?, data_de_captura=?, categoria=? where codigo=?";
        try(PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql))
        {
            ps.setLong(1, pokemon.getCodigo());
            ps.setString(2, pokemon.getNome());
            ps.setDouble(3, pokemon.getAltura());
            ps.setDouble(4, pokemon.getPeso());
            ps.setDate(5, Date.valueOf(pokemon.getDataDeCaptura()));
            ps.setString(6, pokemon.getCategoria());
            if (ps.executeUpdate() > 0) {
                return pokemon;
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar: " + e.getMessage());
        } finally {
            ConnectionFactory.closeConnection();
        }
        return null;
    }

}
