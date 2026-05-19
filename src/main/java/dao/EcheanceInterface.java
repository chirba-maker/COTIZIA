/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import models.Echeance;

/**
 *
 * @author Major117
 */
public interface EcheanceInterface extends GenericDao<Echeance> {
    java.util.List<Echeance> findByCycle(int idCycle);
}
