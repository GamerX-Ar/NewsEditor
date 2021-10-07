package com.hubble.dao;

import com.hubble.data.domain.Dictionary;
import java.sql.SQLException;
import java.util.Map;

public interface IDictionaryDAO extends IObjectDAO<Map.Entry<Integer, Dictionary.Term>> {

    Dictionary.Term getTerm(int dicId, int termCode) throws SQLException;

    Dictionary getDictionary(int dicId, int parentCode, int par1, boolean termSorting);

}
