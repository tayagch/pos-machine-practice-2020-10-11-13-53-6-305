package pos.machine;

import java.util.ArrayList;
import java.util.List;

import static pos.machine.ItemDataLoader.loadAllItemInfos;

public class PosMachine {
    public String printReceipt(List<String> barcodes) {
        List<Items> itemsList = getBarcodeData(barcodes);
        getItemsQuantity(barcodes, itemsList);
        Receipt receipt = calculateReceipt(itemsList);
        return receiptFormatter(receipt);
    }

    private String receiptFormatter(Receipt receipt) {
        StringBuilder receiptString = new StringBuilder("***<store earning no money>Receipt***\n");
        for (Items items : receipt.getItemsFullList()) {
            receiptString.append(String.format("Name: %s, Quantity: %s, Unit price: %s (yuan), Subtotal: %s (yuan)\n",items.getName(),items.getQuantity(),items.getUnitPrice(),items.getSubtotal()));
        }
        receiptString.append("----------------------\n" + "Total: ").append(receipt.getReceiptTotal()).append(" (yuan)\n").append("**********************");
        return receiptString.toString();
    }


    private Receipt calculateReceipt(List<Items> itemsList) {
        calculateSubtotal(itemsList);
        int receiptTotal = calculateTotal(itemsList);
        return new Receipt(itemsList,receiptTotal);
    }

    private int calculateTotal(List<Items> itemsList) {
        int total = 0;
        for (Items items : itemsList) {
            total += items.getSubtotal();
        }
        return total;
    }

    private void calculateSubtotal(List<Items> itemsList) {
        for (Items items : itemsList) {
            items.setSubtotal(items.getQuantity() * items.getUnitPrice());
        }
    }

    private List<Items> getBarcodeData(List<String> barcodes) {
        List<String> uniqueBarcodes = new ArrayList<>();
        List<Items> itemsList = new ArrayList<>();
        
        for (String data : barcodes) {
            if(!uniqueBarcodes.contains(data)){
                uniqueBarcodes.add(data);
            }
        }
        
        List<ItemInfo> itemInfos = loadAllItemInfos();
        for (String barcode : uniqueBarcodes) {
            for (ItemInfo info : itemInfos) {
                if (info.getBarcode().equals(barcode)) {
                    Items items = new Items(info.getBarcode(),info.getName(),0,info.getPrice(),0);
                    itemsList.add(items);
                }
            }
        }

        return itemsList;
    }

    private void getItemsQuantity(List<String> barcodes, List<Items> itemsList) {
        int quantity = 0;
        for (Items items: itemsList) {
            for (String barcode : barcodes) {
                if(items.getBarcode().equals(barcode)){
                    quantity++;
                }
            }
            items.setQuantity(quantity);
            quantity = 0;
        }
    }
}
