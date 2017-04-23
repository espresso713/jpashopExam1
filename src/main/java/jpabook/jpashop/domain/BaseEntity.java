package jpabook.jpashop.domain;

import javax.persistence.MappedSuperclass;
import java.util.Date;

/**
 * Created by hyun on 2017-01-02.
 */
@MappedSuperclass
public class BaseEntity {

    private Date createDate;

    private Date lastModifiedDate;

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
