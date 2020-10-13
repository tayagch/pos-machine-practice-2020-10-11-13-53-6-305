package pos.machine;

import java.util.List;

public class Receipt {
    private List<Items> itemsFullList;
    private int receiptTotal;

    public Receipt(List<Items> itemsFullList, int receiptTotal) {
        this.itemsFullList = itemsFullList;
        this.receiptTotal = receiptTotal;
    }

    public List<Items> getItemsFullList() {
        return itemsFullList;
    }

    public int getReceiptTotal() {
        return receiptTotal;
    }
}
