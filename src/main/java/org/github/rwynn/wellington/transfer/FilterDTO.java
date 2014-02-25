package org.github.rwynn.wellington.transfer;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

public class FilterDTO implements Serializable {

    private String text;

    public FilterDTO(String text) {
        super();
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isBlank() {
        return StringUtils.isBlank(getText());
    }

    public String getWildcardText() {
        return StringUtils.join("%", getText(), "%");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FilterDTO filterDTO = (FilterDTO) o;

        if (text != null ? !text.equals(filterDTO.text) : filterDTO.text != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return text != null ? text.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "FilterDTO{" +
                "text='" + text + '\'' +
                '}';
    }
}
