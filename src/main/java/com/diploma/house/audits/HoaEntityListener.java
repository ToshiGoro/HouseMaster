package com.diploma.house.audits;

import com.diploma.house.entity.Hoa;
import jakarta.persistence.PreRemove;

public class HoaEntityListener {

    @PreRemove
    public void preRemove(Hoa hoa) {
        hoa.clearHouses();
    }

}
