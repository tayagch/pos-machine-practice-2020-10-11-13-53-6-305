package pos.machine;

import java.util.ArrayList;
import java.util.List;

import static pos.machine.ItemDataLoader.loadAllItemInfos;

public class PosMachine {
    public String printReceipt(List<String> barcodes) {
        List<Items> itemsList = getBarcodeData(barcodes);
        itemsList = getItemsQuantity(barcodes,itemsList);
        Receipt receipt = calculateReceipt(itemsList);
        String formatReceipt = receiptFormatter(receipt);
        return formatReceipt;
    }

    private String receiptFormatter(Receipt receipt) {
        String receiptString = "***<store earning no money>Receipt***\n";
        for (Items items : receipt.getItemsFullList()) {
            receiptString += "Name: " + items.getName() + ", Quantity: " + items.getQuantity() + ", Unit price: " + items.getUnitPrice() +
                    " (yuan), Subtotal: " + items.getSubtotal() + " (yuan)\n";
        }
        receiptString += "----------------------\n" +
                "Total: " + receipt.getReceiptTotal() + " (yuan)\n" +
                "**********************";
        return receiptString;
    }


    private Receipt calculateReceipt(List<Items> itemsList) {
        itemsList = calculateSubtotal(itemsList);
        int receiptTotal = calculateTotal(itemsList);
        Receipt receipt = new Receipt(itemsList,receiptTotal);
        return receipt;
    }

    private int calculateTotal(List<Items> itemsList) {
        int total = 0;
        for (Items items : itemsList) {
            total += items.getSubtotal();
        }
        return total;
    }

    private List<Items> calculateSubtotal(List<Items> itemsList) {
        for (Items items : itemsList) {
            items.setSubtotal(items.getQuantity() * items.getUnitPrice());
        }
        return itemsList;
    }

    private List<Items> getBarcodeData(List<String> barcodes) {
        List<String> uniqueBarcodes = new ArrayList<>();
        List<Items> itemsList = new ArrayList<>();
        
        for (String data : barcodes) {
            if(uniqueBarcodes.contains(data)){
                continue;
            }
            else uniqueBarcodes.add(data);
        }
        
        List<ItemInfo> itemInfos = loadAllItemInfos();
        for (String barcode : uniqueBarcodes) {
            for (ItemInfo info : itemInfos) {
                if (info.getBarcode() == barcode) {
                    Items items = new Items(info.getBarcode(),info.getName(),0,info.getPrice(),0);
                    itemsList.add(items);
                }
            }
        }

        return itemsList;
    }

    private List<Items> getItemsQuantity(List<String> barcodes, List<Items> itemsList) {
        int quantity = 0;
        for (Items items: itemsList) {
            for (String barcode : barcodes) {
                if(items.getBarcode() == barcode){
                    quantity++;
                }
            }
            items.setQuantity(quantity);
            quantity = 0;
        }
        return itemsList;
    }
}
