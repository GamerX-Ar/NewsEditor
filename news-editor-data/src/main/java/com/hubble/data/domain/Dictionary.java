package com.hubble.data.domain;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>Словарь терминов КСИ.</p>
 *
 * @version 1.1
 * @see Term
 */
public final class Dictionary extends LinkedHashMap<Integer, Dictionary.Term> {

    private static final long serialVersionUID = 9181952300519489707L;

    public enum Type {

          EVENTS(4)
        , CITIES(12)
        , COUNTRIES(13)
        , REGIONS(14)
        , OBJECTS(23)
        , ERROR_CODES(36)
        , NEWS_STATUSES(41)
        , DISTRICTS(42)
        , DOCUMENTS(44)
        ;

        private final int code;

        Type(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        public static Type valueOf(int code) {
            for ( Type type : Type.values() ) {
                if (type.code == code) return type;
            }
            return null;
        }
    }

    /**
     * <p>Термин КСИ.</p>
     *
     * @version 1.1
     */
    public static final class Term extends AbstractVersionObject implements Serializable {

        private static final long serialVersionUID = 186964963294809000L;

        private String term;
        private String term2;
        private Integer parentCode;
        private Integer par1;

        public Term(String term) {
            this.term = term;
        }

        public Term() {

        }

        public String getTerm() {
            return term;
        }

        public void setTerm(String term) {
            this.term = term;
        }

        public String getTerm2() { return term2; }

        public void setTerm2(String term2) { this.term2 = term2; }

        public Integer getParentCode() {
            return parentCode;
        }

        public void setParentCode(Integer parentCode) {
            this.parentCode = parentCode;
        }

        public Integer getPar1() {
            return par1;
        }

        public void setPar1(Integer par1) {
            this.par1 = par1;
        }

        @Override
        public String toString() {
            return (new JSONObject(this)).toString();
        }
    }

    /**
     * Constructs an insertion-ordered <tt>Dictionary</tt> instance with
     * the same mappings as the specified map.  The <tt>Dictionary</tt>
     * instance is created with a default load factor (0.75) and an initial
     * capacity sufficient to hold the mappings in the specified map.
     *
     * @param m the map whose mappings are to be placed in this map
     * @throws NullPointerException if the specified map is null
     */
    public Dictionary(Map<? extends Integer, ? extends Term> m) {
        super(m);
    }

    /**
     * Constructs an empty insertion-ordered <tt>Dictionary</tt> instance
     * with the default initial capacity (16) and load factor (0.75).
     */
    public Dictionary() {
        super();
    }

    public Term put(Map.Entry<Integer, Term> t) {
        return super.put(t.getKey(), t.getValue());
    }

    public JSONArray toJsonArray() {
        JSONArray jsonArray = new JSONArray();
        this.forEach((k, v) -> jsonArray.put(new JSONArray().put(k.toString()).put(v.getTerm())));
        return jsonArray;
    }

    @Override
    public String toString() {
        return (new JSONObject(this)).toString();
    }
}