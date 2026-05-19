/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import models.Adherent;

/**
 *
 * @author Major117
 */
public interface AdherentInterface extends GenericDao<Adherent> {
    java.util.List<Adherent> search(String query);
}
