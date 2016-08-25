package kr.co.d2net.netbro.common.model;


public abstract class BaseObject {

	@Override
    public boolean equals(Object obj) {
        return BeanUtils.equals(this, obj);
    }

    @Override
    public int hashCode() {
        return BeanUtils.hashCode(this);
    }

    @Override
    public String toString() {
        return BeanUtils.toString(this);
    }
    
}
