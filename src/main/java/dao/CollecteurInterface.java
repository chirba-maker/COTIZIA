/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import models.Collecteur;

/**
 *
 * @author Major117
 */
public interface CollecteurInterface extends GenericDao<Collecteur> {
    Collecteur findByUserId(int userId);
}
