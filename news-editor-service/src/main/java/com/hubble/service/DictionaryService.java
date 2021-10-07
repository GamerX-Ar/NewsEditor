package com.hubble.service;

import com.hubble.data.domain.Dictionary;
import com.hubble.jdbc.DictionaryDAO;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import java.sql.SQLException;
import java.util.Map;

public class DictionaryService {

    private static final Cache dicCache = CacheManager.getInstance().getCache("DictionaryCache");

    /**
     * Возвращает актуальный термин словаря.
     *
     * @param termN СН термина
     * @return термин словаря <code>Term</code>
     * @throws SQLException если не удалось выполнить запрос
     */
    public Map.Entry<Integer, Dictionary.Term> getTerm(long termN) throws SQLException {
        return new DictionaryDAO().getById(termN);
    }

    /**
     * Возвращает актуальный термин словаря.
     *
     * @param type тип словаря
     * @param termCode код термина
     * @return термин словаря <code>Term</code>
     * @throws SQLException если не удалось выполнить запрос
     */
    public Dictionary.Term getTerm(Dictionary.Type type, int termCode) throws SQLException {
        return new DictionaryDAO().getTerm(type.getCode(), termCode);
    }

    /**
     * Вазвращает актуальный сортированый список терминов словаря.
     *
     * @param type тип словаря
     * @param parentCode код родительского термина
     * @param termSorting выполнить сортировку по терминам
     * @return Словарь <code>Dictionary</code>
     */
    public static Dictionary getDictionary(Dictionary.Type type, Integer parentCode, Integer par1, boolean termSorting) {
        String key = type.toString() + parentCode + par1 + termSorting;
        Element dicElement = dicCache.get(key);

        if (dicElement == null) {
            Dictionary dic = new DictionaryDAO().getDictionary(
                    type.getCode(),
                    parentCode == null ? -1 : parentCode,
                    par1 == null ? -1 : par1,
                    termSorting
            );
            dicCache.put(new Element(key, dic));
            return dic;
        } else {
            return (Dictionary) dicElement.getObjectValue();
        }
    }

    /**
     * Вазвращает актуальный список терминов словаря, отсортированый по кодам.
     *
     * @param type Тип словаря
     * @param parentCode Код родительского термина
     * @param par1 Дополнительный параметр
     * @return Словарь <code>Dictionary</code>
     */
    public static Dictionary getDictionary(Dictionary.Type type, Integer parentCode, Integer par1) {
        return getDictionary(type, parentCode, par1, false);
    }

    /**
     * Вазвращает актуальный список терминов словаря, отсортированый по кодам.
     *
     * @param type тип словаря
     * @param parentCode код родительского термина
     * @return Словарь <code>Dictionary</code>
     */
    public static Dictionary getDictionary(Dictionary.Type type, Integer parentCode) {
        return getDictionary(type, parentCode, null, false);
    }

    /**
     * Вазвращает актуальный список терминов словаря, отсортированый по кодам.
     *
     * @param type тип словаря
     * @return Словарь <code>Dictionary</code>
     */
    public static Dictionary getDictionary(Dictionary.Type type) {
        return getDictionary(type, null, null, false);
    }

}
