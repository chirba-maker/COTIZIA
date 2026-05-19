/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import java.util.List;

/**
 *
 * @author Major117
 */
public interface GenericDao <T> {
    void save(T t);
    void update (T t);
    List<T> getAll();
    void delete (T t);
    T findById (int id);
}
