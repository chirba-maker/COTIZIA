/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import models.Utilisateur;

/**
 *
 * @author Major117
 */
public interface UtilisateurInterface extends GenericDao<Utilisateur>{
	// Find a user by login (used by AuthService)
	Utilisateur findByLogin(String login);
}
