package com.hubble.tags;

import com.hubble.data.domain.Dictionary;
import com.hubble.service.DictionaryService;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.SkipPageException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

public class DictionaryTagHandler extends SimpleTagSupport {

    private String var;
    private Dictionary.Type type;
    private Integer parentCode; // R_PARENT_TERM_CODE
    private Integer par; // PAR1
    private Boolean order = false;

    @Override
    public void doTag() throws JspException, IOException {
        Dictionary dict;
        try {
            dict = DictionaryService.getDictionary(type, parentCode, par, order);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new SkipPageException("Something wrong!", e);
        }
        getJspContext().setAttribute(var, dict);
    }

    public void setVar(String var) {
        this.var = var;
    }

    public void setType(Dictionary.Type type) {
        this.type = type;
    }

    /**
     *
     * Если в теге указан соответствующий атрибут (<code>parentCode</code>), необходимо использовать его в
     * выборке, независимо от того, чему он равен. Когда <code>parentCode == null</code> (вытащи то, не знаю что)
     * интуитивно ожидаешь выборку <code>where parent_code = null</code> и эта выборка должна быть пустой.
     * Для достижения этой цели необходимо различать случаи, когда <code>parentCode</code> указан в теге и
     * когда не указан. Т.к. переменные в Java по умолчанию инициализируются <code>null</code>'ом, и атрибут
     * в теге тоже может быть <code>null</code> - в послденем случае присовем 0 соотвествующей атрибуту переменной.
     * Тогда <code>null</code> - атрибут не использован, 0 - использован, но имеет значение <code>null</code> и
     * ожидается пустой список.
     *
     * @param parentCode Родительский код терминов.
     */
    public void setParentCode(Integer parentCode) {
        this.parentCode = parentCode == null ? 0 : parentCode;
    }

    /**
     *
     * Если в теге указан соответствующий атрибут (<code>par</code>), необходимо использовать его в
     * выборке, независимо от того, чему он равен. Когда <code>par == null</code> (вытащи то, не знаю что)
     * интуитивно ожидаешь выборку <code>where par = null</code> и эта выборка должна быть пустой.
     * Для достижения этой цели необходимо различать случаи, когда <code>par</code> указан в теге и
     * когда не указан. Т.к. переменные в Java по умолчанию инициализируются <code>null</code>'ом, и атрибут
     * в теге тоже может быть <code>null</code> - в послденем случае присовем 0 соотвествующей атрибуту переменной.
     * Тогда <code>null</code> - атрибут не использован, 0 - использован, но имеет значение <code>null</code> и
     * ожидается пустой список.
     *
     * @param par Родительский код терминов.
     */
    public void setPar(Integer par) {
        this.par = par == null ? 0 : par;
    }

    public void setOrder(Boolean order) {
        this.order = order;
    }
}
