package service;

import model.Bill;

import repository.FileStore;
import util.JSONHelper;
import util.Logger;
import java.util.ArrayList;
import java.util.List;

public class BillingService {
    private String businessPath;

    public BillingService(String businessPath) {
        this.businessPath = businessPath;
    }

    public void saveBill(Bill bill) {
        // Direct file save for bills as they are append-only usually
        // Using a similar logic to FileRepository but specialized for Bills
        String billsPath = businessPath + "/bills.json";
        String json = FileStore.getInstance().read(billsPath);

        // Simpler approach: Just read internal list, add, write.
        // Since we don't have a BillRepository specifically in the plan (we could have
        // added one),
        // we will do a direct JSON manipulation or standard repo-like behavior here.
        // Actually, let's create a mini-repo logic inline or assume we just append to
        // the array.

        // For the sake of the exercise, let's just re-read the array as a generic list
        // of maps,
        // add the new bill map, and write it back.
        Object existing = JSONHelper.parse(json);
        List<Object> billsList;
        if (existing instanceof List) {
            @SuppressWarnings("unchecked")
            List<Object> casted = (List<Object>) existing;
            billsList = casted;
        } else {
            billsList = new ArrayList<>();
        }

        // We need to convert Bill to Map or rely on JSONHelper's reflection
        // JSONHelper.toJson(bill) gives string.
        // We can parse that back to Map to add to the list, or just maintain list of
        // Bills if we could parse them back.
        // Since we didn't write a Map->Bill parser yet, let's just act as if we loaded
        // them.

        // Optimization: Let's just create a new list, add all current maps, add new
        // bill, save.
        // But we need to convert Bill to Map.
        // Let's just use the String manipulation for now? No, that's risky.
        // Let's trust JSONHelper to serialize the whole list including the new object
        // properly if we had the objects.
        // Since we don't load old objects, we lose them?
        // Required: We must preserve old data.

        // Strategy: Parse old JSON to List<Map>, add new Bill (as Map or Object),
        // Write.
        // JSONHelper.toJson handles List<Object> where Object can be Map or POJO.
        // mixing is fine.

        billsList.add(bill);
        String newJson = JSONHelper.toJson(billsList);
        FileStore.getInstance().writeResult(billsPath, newJson);

        Logger.getInstance().info("Bill saved: " + bill.getId());
    }
}
