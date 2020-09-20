package com.galaplat.comprehensive.bidding.netty.channel;

public class ClientChannelManager extends ChannelComposite implements ClientChannelEvent, ConstMark {

    @Override
    public void supplierEvent(String type, Object message) {
        event(type,SUPPLIER_TYPE,message);
    }

    @Override
    public void adminEvent(String type, Object message) {
        event(type, ADMIN_TYPE, message);
    }

    public void event(String type, String clientType, Object message) {
        switch (type) {
            case ADD:
                doNotifyAddEvent(clientType, message);
                break;

            case RMV:
                doNotifyRemoveEvent(clientType, message);
                break;
        }

    }

    private void doNotifyAddEvent(String clientType, Object message) {
        switch (clientType) {
            case SUPPLIER_TYPE:
                //do supplier add notify
                break;
            case ADMIN_TYPE:
                //do admin add notify
                break;
        }
    }

    private void doNotifyRemoveEvent(String clientType, Object message) {
        switch (clientType) {
            case SUPPLIER_TYPE:
                //do supplier remove notify
                break;
            case ADMIN_TYPE:
                //do admin remove notify
                break;
        }
    }


}
