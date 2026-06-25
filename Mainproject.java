package project;
import java.util.*;

public class Mainproject {

    // ============================================================
    //  BST NODE
    // ============================================================
    static class MedicineNode {
        int medicineId;
        String name;
        int stock;
        double price;
        MedicineNode right, left;
    }

    // Helper struct for sorting / knapsack
    static class MedicineRecord {
        int medicineId;
        String name;
        int stock;
        double price;
        MedicineRecord(MedicineNode n) {
            medicineId = n.medicineId;
            name       = n.name;
            stock      = n.stock;
            price      = n.price;
        }
    }

    static MedicineNode root = null;
    static MedicineNode ptr, pre;
    static Scanner sc = new Scanner(System.in);

    // ============================================================
    //  MAIN MENU
    // ============================================================
    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=======================================================");
            System.out.println("          PHARMATRACK INTELLIGENT SYSTEM DASHBOARD     ");
            System.out.println("=======================================================");
            System.out.println("--- MODULE 1 & 2: INVENTORY LOGIC (CO1 & CO2) ---");
            System.out.println("  1.  Add Medicine Stock (BST Insert)");
            System.out.println("  2.  Search Highest Stock Medicine");
            System.out.println("  3.  Search Lowest Stock Medicine");
            System.out.println("  4.  View Sorted Medicine Catalog (Inorder by ID)");
            System.out.println("  5.  Delete Medicine from Inventory");
            System.out.println("  6. Generate Customer Prescription / Bill");
            System.out.println("\n--- MODULE 3 & 4: LOGISTICS GRID (CO3 & CO4) ---");
            System.out.println("  7.  Check Warehouse Network Connectivity (BFS)");
            System.out.println("  8.  Detect Supply Chain Cycles (DFS)");
            System.out.println("  9.  Optimize Hospital Delivery Route (Dijkstra)");
            System.out.println("\n--- MODULE 5 & 6: ANALYTICS & GREEDY (CO5 & CO6) ---");
            System.out.println("  10.  Sort Medicine by Stock (Heap / Quick / Merge)");
            System.out.println("  11. Optimize Truck Shipment Load (Fractional Knapsack)");
            System.out.println("  12. Exit System");
            System.out.println("=======================================================");
            System.out.print("Enter your choice: ");
            int ch = sc.nextInt();

            switch (ch) {
            case 1:  create(); break;
            case 2:  max(); break;
            case 3:  min(); break;
            case 4:  
                System.out.println("\n--- Master Inventory List (sorted by ID) ---");
                inorder(root); break;
            case 5:  delete(); break;
            case 6:  prescription(); break;
            case 7:  runBFS(); break;
            case 8:  runDFS(); break;
            case 9:  runDijkstra(); break;
            case 10:   runSortingDashboard();  break;
            case 11:  runFractionalKnapsack(); break;
case 12:   System.out.println("Shutting down PharmaTrack...");   System.exit(0);  break;
            default:  System.out.println("Invalid choice!");
        }
        }
    }

    // ============================================================
    //  OPTION 1 — ADD MEDICINE (BST insert by ID)
    // ============================================================
    static void create() {
        MedicineNode nn = new MedicineNode();
        System.out.print("Enter Medicine ID   : ");
        int x = sc.nextInt(); sc.nextLine();
        if (search(root, x) != null) {
            System.out.println("Medicine ID " + x + " already exists. Use a unique ID.");
            return;
        }
        System.out.print("Enter Medicine Name  : "); nn.name  = sc.nextLine();
        System.out.print("Enter Stock Quantity : "); nn.stock = sc.nextInt();
        System.out.print("Enter Price per Unit : Rs."); nn.price = sc.nextDouble();
        nn.medicineId = x; nn.left = nn.right = null;

        if (root == null) { root = nn; }
        else {
            ptr = root;
            while (ptr != null) {
                pre = ptr;
                ptr = (x > ptr.medicineId) ? ptr.right : ptr.left;
            }
            if (x > pre.medicineId) pre.right = nn; else pre.left = nn;
        }
        System.out.println("Medicine '" + nn.name + "' added successfully.");
    }

    // ============================================================
    //  OPTION 2 — HIGHEST STOCK
    // ============================================================
    static void max() {
        if (root == null) { System.out.println("Inventory is empty."); return; }
        MedicineNode r = findMaxStock(root);
        System.out.println("\n[HIGHEST STOCK MEDICINE]");
        printMed(r);
    }
    static MedicineNode findMaxStock(MedicineNode n) {
        if (n == null) return null;
        MedicineNode l = findMaxStock(n.left), r = findMaxStock(n.right), m = n;
        if (l != null && l.stock > m.stock) m = l;
        if (r != null && r.stock > m.stock) m = r;
        return m;
    }

    // ============================================================
    //  OPTION 3 — LOWEST STOCK
    // ============================================================
    static void min() {
        if (root == null) { System.out.println("Inventory is empty."); return; }
        MedicineNode r = findMinStock(root);
        System.out.println("\n[LOWEST STOCK MEDICINE — Consider Restocking]");
        printMed(r);
    }
    static MedicineNode findMinStock(MedicineNode n) {
        if (n == null) return null;
        MedicineNode l = findMinStock(n.left), r = findMinStock(n.right), m = n;
        if (l != null && l.stock < m.stock) m = l;
        if (r != null && r.stock < m.stock) m = r;
        return m;
    }

    static void printMed(MedicineNode n) {
        System.out.printf("  ID    : %d%n  Name  : %s%n  Stock : %d units%n  Price : Rs.%.2f%n",
            n.medicineId, n.name, n.stock, n.price);
    }

    // ============================================================
    //  OPTION 4 — INORDER (by ID ascending)
    // ============================================================
    public static void inorder(MedicineNode p) {
        if (p != null) {
            inorder(p.left);
            System.out.printf("  ID: %-5d | %-22s | Stock: %-6d | Price: Rs.%.2f%n",
                p.medicineId, p.name, p.stock, p.price);
            inorder(p.right);
        }
    }

    // ============================================================
    //  HELPER — collect ALL BST nodes into a list
    // ============================================================
    static void collectAll(MedicineNode n, List<MedicineRecord> list) {
        if (n == null) return;
        collectAll(n.left, list);
        list.add(new MedicineRecord(n));
        collectAll(n.right, list);
    }

    // ============================================================
    //  HELPER — BST search by ID
    // ============================================================
    static MedicineNode search(MedicineNode n, int id) {
        while (n != null) {
            if      (id == n.medicineId) return n;
            else if (id <  n.medicineId) n = n.left;
            else                         n = n.right;
        }
        return null;
    }

    // ============================================================
    //  OPTION 12 — PRESCRIPTION / BILLING
    // ============================================================
    static void prescription() {
        if (root == null) { System.out.println("No medicines in inventory."); return; }
        System.out.println("\n============================================");
        System.out.println("        CUSTOMER PRESCRIPTION MODULE        ");
        System.out.println("============================================");
        inorder(root);
        System.out.println("--------------------------------------------");
        double total = 0; int lines = 0;
        while (true) {
            System.out.print("\nEnter Medicine ID (0 to finish): ");
            int id = sc.nextInt();
            if (id == 0) break;
            MedicineNode med = search(root, id);
            if (med == null) { System.out.println("  [!] Not found."); continue; }
            System.out.printf("  %s | Stock: %d | Rs.%.2f/unit%n", med.name, med.stock, med.price);
            System.out.print("  Quantity: ");
            int qty = sc.nextInt();
            if (qty <= 0)         { System.out.println("  [!] Invalid quantity."); continue; }
            if (qty > med.stock)  { System.out.println("  [!] Only " + med.stock + " units available."); continue; }
            double cost = qty * med.price;
            med.stock -= qty; total += cost; lines++;
            System.out.printf("  Added: %d x %s = Rs.%.2f  (Remaining stock: %d)%n", qty, med.name, cost, med.stock);
        }
        System.out.println("\n============================================");
        System.out.println("              PRESCRIPTION BILL             ");
        System.out.println("============================================");
        if (lines == 0) System.out.println("  No items dispensed.");
        else System.out.printf("  Items: %d line(s)%n  TOTAL AMOUNT : Rs.%.2f%n", lines, total);
        System.out.println("============================================");
    }

    // ============================================================
    //  OPTION 5 — DELETE
    // ============================================================
    public static void delete() {
        if (root == null) { System.out.println("Inventory is empty."); return; }
        System.out.print("Enter Medicine ID to remove: ");
        int x = sc.nextInt();
        if (search(root, x) == null) { System.out.println("ID " + x + " not found."); return; }
        root = deleteRec(root, x);
        System.out.println("Medicine ID " + x + " removed.");
    }
    static MedicineNode deleteRec(MedicineNode n, int key) {
        if (n == null) return null;
        if      (key < n.medicineId) n.left  = deleteRec(n.left,  key);
        else if (key > n.medicineId) n.right = deleteRec(n.right, key);
        else {
            if (n.left  == null) return n.right;
            if (n.right == null) return n.left;
            MedicineNode t = n.right;
            while (t.left != null) t = t.left;
            n.medicineId = t.medicineId; n.name = t.name; n.stock = t.stock; n.price = t.price;
            n.right = deleteRec(n.right, t.medicineId);
        }
        return n;
    }

    // ============================================================
    //  OPTION 6 — BFS: WAREHOUSE NETWORK CONNECTIVITY
    //
    //  Real use: You have multiple warehouses / distribution depots.
    //  Enter how many, give their names, enter the road/link matrix,
    //  pick a starting depot — BFS tells you which depots are
    //  reachable (connected) from it and which are isolated.
    //
    //  Input:
    //    n               — number of depots
    //    n depot names   — one per line
    //    n×n matrix      — 1 if direct link exists, 0 otherwise
    //    start index
    // ============================================================
    public static void runBFS() {
        System.out.println("\n[WAREHOUSE NETWORK — BFS CONNECTIVITY CHECK]");
        System.out.print("Enter number of warehouses/depots: ");
        int n = sc.nextInt(); sc.nextLine();

        String[] depots = new String[n];
        System.out.println("Enter depot names:");
        for (int i = 0; i < n; i++) {
            System.out.print("  Depot " + i + ": ");
            depots[i] = sc.nextLine();
        }

        int[][] a = new int[n][n];
        System.out.println("Enter connectivity matrix (1=linked, 0=no link):");
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                a[i][j] = sc.nextInt();

        System.out.print("Enter starting depot index: ");
        int start = sc.nextInt();

        boolean[] visited = new boolean[n];
        Queue<Integer> q = new LinkedList<>();
        q.add(start); visited[start] = true;

        System.out.println("\nBFS Traversal from [" + depots[start] + "]:");
        int order = 1;
        List<Integer> reachable = new ArrayList<>();
        while (!q.isEmpty()) {
            int v = q.remove();
            System.out.println("  Step " + order++ + ": Reached [" + depots[v] + "]");
            reachable.add(v);
            for (int i = 0; i < n; i++)
                if (a[v][i] != 0 && !visited[i]) { visited[i] = true; q.add(i); }
        }

        System.out.println("\n--- Connectivity Report ---");
        boolean allOk = true;
        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                System.out.println("  [ISOLATED] " + depots[i] + " — cannot receive supplies from " + depots[start]);
                allOk = false;
            }
        }
        if (allOk)
            System.out.println("  All depots are reachable. Supply network is FULLY CONNECTED.");
        else
            System.out.println("  WARNING: Isolated depots detected. Review supply links.");
    }

    // ============================================================
    //  OPTION 7 — DFS: DETECT SUPPLY CHAIN CYCLES
    //
    //  Real use: In a directed supply chain (Supplier -> Warehouse
    //  -> Distributor -> Pharmacy), a cycle means circular dependency
    //  (e.g. Warehouse re-orders from its own downstream pharmacy).
    //  DFS with back-edge detection finds such cycles.
    //
    //  Input:
    //    n               — number of supply nodes
    //    n node names    — one per line
    //    n×n matrix      — directed adjacency (1 = A supplies B)
    //    start index
    // ============================================================
    public static void runDFS() {
        System.out.println("\n[SUPPLY CHAIN — DFS CYCLE DETECTION]");
        System.out.print("Enter number of supply chain nodes: ");
        int n = sc.nextInt(); sc.nextLine();

        String[] nodes = new String[n];
        System.out.println("Enter node names (e.g. Supplier, Warehouse, Pharmacy):");
        for (int i = 0; i < n; i++) {
            System.out.print("  Node " + i + ": ");
            nodes[i] = sc.nextLine();
        }

        int[][] a = new int[n][n];
        System.out.println("Enter directed adjacency matrix (1 = supplies, 0 = no link):");
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                a[i][j] = sc.nextInt();

        System.out.print("Enter start node index: ");
        int start = sc.nextInt();

        boolean[] visited  = new boolean[n];
        boolean[] inStack  = new boolean[n];   // tracks current DFS path
        boolean   cycleFound = false;

        // Iterative DFS with parent tracking for cycle detection
        Stack<int[]> st = new Stack<>();  // [node, parentNode]
        st.push(new int[]{start, -1});

        dfsStep = 1;
        System.out.println("\nDFS Traversal from [" + nodes[start] + "]:");

        // Recursive helper for proper back-edge detection
        cycleFound = dfsHelper(start, a, visited, inStack, nodes, dfsStep);

        System.out.println("\n--- Cycle Detection Report ---");
        if (cycleFound)
            System.out.println("  CYCLE DETECTED in supply chain! Circular dependency found.");
        else
            System.out.println("  No cycles found. Supply chain flow is LINEAR and valid.");
    }

    static int dfsStep = 1;
    static boolean dfsHelper(int v, int[][] a, boolean[] visited, boolean[] inStack, String[] names, int step) {
        visited[v] = true;
        inStack[v] = true;
        System.out.println("  Step " + dfsStep++ + ": Visiting [" + names[v] + "]");

        boolean cycleFound = false;
        for (int i = 0; i < a[v].length; i++) {
            if (a[v][i] != 0) {
                if (!visited[i]) {
                    if (dfsHelper(i, a, visited, inStack, names, dfsStep)) cycleFound = true;
                } else if (inStack[i]) {
                    System.out.println("  [!] Back-edge detected: [" + names[v] + "] --> [" + names[i] + "] — CYCLE!");
                    cycleFound = true;
                }
            }
        }
        inStack[v] = false;
        return cycleFound;
    }

    // ============================================================
    //  OPTION 8 — DIJKSTRA: HOSPITAL DELIVERY ROUTE
    // ============================================================
    public static void runDijkstra() {
        System.out.println("\n========================================");
        System.out.println("  MEDICINE DELIVERY — ROUTE OPTIMIZER  ");
        System.out.println("========================================");
        System.out.print("Enter number of locations: ");
        int n = sc.nextInt(); sc.nextLine();

        String[] loc = new String[n];
        System.out.println("Enter location names:");
        for (int i = 0; i < n; i++) { System.out.print("  Location " + i + ": "); loc[i] = sc.nextLine(); }

        int[][] dist = new int[n][n];
        System.out.println("Enter distance matrix (km, 0 = no direct road):");
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                dist[i][j] = sc.nextInt();

        System.out.print("Enter source index       : "); int src  = sc.nextInt();
        System.out.print("Enter destination index  : "); int dest = sc.nextInt();

        int[] d = new int[n], parent = new int[n];
        boolean[] vis = new boolean[n];
        Arrays.fill(d, Integer.MAX_VALUE); Arrays.fill(parent, -1);
        d[src] = 0;

        for (int i = 0; i < n; i++) {
            int min = Integer.MAX_VALUE, u = -1;
            for (int j = 0; j < n; j++) if (!vis[j] && d[j] < min) { min = d[j]; u = j; }
            if (u == -1) break;
            vis[u] = true;
            for (int j = 0; j < n; j++)
                if (dist[u][j] != 0 && !vis[j] && d[u] != Integer.MAX_VALUE && d[u] + dist[u][j] < d[j]) {
                    d[j] = d[u] + dist[u][j]; parent[j] = u;
                }
        }

        System.out.println("\n--- Distances from [" + loc[src] + "] ---");
        for (int i = 0; i < n; i++)
            System.out.printf("  To %-25s : %s%n", loc[i], d[i] == Integer.MAX_VALUE ? "Unreachable" : d[i] + " km");

        System.out.println("\n--- Optimal Route to [" + loc[dest] + "] ---");
        if (d[dest] == Integer.MAX_VALUE) {
            System.out.println("  No route available.");
        } else {
            List<Integer> path = new ArrayList<>();
            for (int at = dest; at != -1; at = parent[at]) path.add(at);
            Collections.reverse(path);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < path.size(); i++) {
                sb.append(loc[path.get(i)]);
                if (i < path.size() - 1) sb.append(" --> ");
            }
            System.out.println("  Route    : " + sb);
            System.out.println("  Distance : " + d[dest] + " km");
        }
        System.out.println("========================================");
    }

    // ============================================================
    //  OPTION 9 — SORT MEDICINES BY STOCK
    //
    //  Reads all medicines currently in the BST inventory,
    //  then asks which algorithm to use:
    //    H = Heap Sort   (uses a max-heap, extracts in order)
    //    Q = Quick Sort  (pivot-based divide & conquer)
    //    M = Merge Sort  (stable divide & merge)
    //  All sort by stock quantity (ascending = low stock first,
    //  so pharmacist can immediately see what needs restocking).
    // ============================================================
    public static void runSortingDashboard() {
        if (root == null) { System.out.println("Inventory is empty. Add medicines first."); return; }

        List<MedicineRecord> list = new ArrayList<>();
        collectAll(root, list);
        MedicineRecord[] arr = list.toArray(new MedicineRecord[0]);
        int n = arr.length;

        System.out.println("\n[MEDICINE STOCK RANKING — SORT BY STOCK QUANTITY]");
        System.out.println("Total medicines in inventory: " + n);
        System.out.println("Choose sorting algorithm:");
        System.out.println("  H = Heap Sort");
        System.out.println("  Q = Quick Sort");
        System.out.println("  M = Merge Sort");
        System.out.print("Enter choice (H/Q/M): ");
        char algo = sc.next().toUpperCase().charAt(0);

        long start = System.nanoTime();
        switch (algo) {
            case 'H': heapSort(arr, n);          break;
            case 'Q': quickSort(arr, 0, n - 1);  break;
            case 'M': mergeSort(arr, 0, n - 1);  break;
            default:  System.out.println("Invalid choice. Using Heap Sort.");
                      heapSort(arr, n);
        }
        long timeTaken = System.nanoTime() - start;

        System.out.println("\n--- Medicines Sorted by Stock (LOW to HIGH) ---");
        System.out.printf("  %-5s | %-22s | %-10s | %s%n", "ID", "Name", "Stock", "Price");
        System.out.println("  " + "-".repeat(60));
        for (MedicineRecord r : arr)
            System.out.printf("  %-5d | %-22s | %-10d | Rs.%.2f%n", r.medicineId, r.name, r.stock, r.price);

        System.out.println("\n  [!] Medicines with lowest stock (top of list) need urgent restocking.");
        System.out.printf("  Sort completed in %.3f ms%n", timeTaken / 1_000_000.0);
    }

    // --- Heap Sort (by stock ascending) ---
    static void heapSort(MedicineRecord[] arr, int n) {
        // Build max-heap
        for (int i = n / 2 - 1; i >= 0; i--) heapify(arr, n, i);
        // Extract one by one
        for (int i = n - 1; i > 0; i--) {
            MedicineRecord tmp = arr[0]; arr[0] = arr[i]; arr[i] = tmp;
            heapify(arr, i, 0);
        }
        // Result is ascending after extraction
    }
    static void heapify(MedicineRecord[] arr, int n, int i) {
        int largest = i, l = 2*i+1, r = 2*i+2;
        if (l < n && arr[l].stock > arr[largest].stock) largest = l;
        if (r < n && arr[r].stock > arr[largest].stock) largest = r;
        if (largest != i) {
            MedicineRecord tmp = arr[i]; arr[i] = arr[largest]; arr[largest] = tmp;
            heapify(arr, n, largest);
        }
    }

    // --- Quick Sort (by stock ascending) ---
    static void quickSort(MedicineRecord[] arr, int low, int high) {
        if (low < high) {
            int pi = partition(arr, low, high);
            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }
    }
    static int partition(MedicineRecord[] arr, int low, int high) {
        int pivot = arr[high].stock;
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (arr[j].stock <= pivot) {
                i++;
                MedicineRecord tmp = arr[i]; arr[i] = arr[j]; arr[j] = tmp;
            }
        }
        MedicineRecord tmp = arr[i+1]; arr[i+1] = arr[high]; arr[high] = tmp;
        return i + 1;
    }

    // --- Merge Sort (by stock ascending) ---
    static void mergeSort(MedicineRecord[] arr, int l, int r) {
        if (l < r) {
            int m = (l + r) / 2;
            mergeSort(arr, l, m);
            mergeSort(arr, m + 1, r);
            merge(arr, l, m, r);
        }
    }
    static void merge(MedicineRecord[] arr, int l, int m, int r) {
        int n1 = m - l + 1, n2 = r - m;
        MedicineRecord[] L = new MedicineRecord[n1], R = new MedicineRecord[n2];
        for (int i = 0; i < n1; i++) L[i] = arr[l + i];
        for (int j = 0; j < n2; j++) R[j] = arr[m + 1 + j];
        int i = 0, j = 0, k = l;
        while (i < n1 && j < n2)
            arr[k++] = (L[i].stock <= R[j].stock) ? L[i++] : R[j++];
        while (i < n1) arr[k++] = L[i++];
        while (j < n2) arr[k++] = R[j++];
    }

    // ============================================================
    //  OPTION 10 — FRACTIONAL KNAPSACK: TRUCK LOAD OPTIMIZER
    //
    //  Real use: A delivery truck has a fixed weight capacity (kg).
    //  You have multiple medicine shipments, each with a weight and
    //  a declared value (Rs.). Fractional knapsack greedily picks
    //  by best value-per-kg ratio, allowing partial shipments,
    //  to maximise the value of medicines loaded onto the truck.
    //
    //  Input:
    //    truck capacity (kg)
    //    number of medicine shipments
    //    for each: name, weight (kg), value (Rs.)
    // ============================================================
    public static void runFractionalKnapsack() {
        System.out.println("\n[TRUCK SHIPMENT OPTIMIZER — FRACTIONAL KNAPSACK]");
        System.out.print("Enter truck capacity (kg): ");
        double capacity = sc.nextDouble();
        System.out.print("Enter number of medicine shipments: ");
        int n = sc.nextInt(); sc.nextLine();

        String[] names  = new String[n];
        double[] weight = new double[n];
        double[] value  = new double[n];
        double[] ratio  = new double[n];

        System.out.println("Enter shipment details:");
        for (int i = 0; i < n; i++) {
            System.out.print("  Shipment " + (i+1) + " name   : "); names[i]  = sc.nextLine();
            System.out.print("  Weight (kg)            : "); weight[i] = sc.nextDouble();
            System.out.print("  Value (Rs.)            : "); value[i]  = sc.nextDouble(); sc.nextLine();
            ratio[i] = value[i] / weight[i];
        }

        // Sort by value/weight ratio descending (greedy choice)
        Integer[] idx = new Integer[n];
        for (int i = 0; i < n; i++) idx[i] = i;
        Arrays.sort(idx, (a, b) -> Double.compare(ratio[b], ratio[a]));

        double totalValue  = 0;
        double remaining   = capacity;

        System.out.println("\n--- Truck Loading Plan ---");
        System.out.printf("  %-25s | %-12s | %-12s | %-10s | Loaded%n",
            "Shipment", "Weight(kg)", "Value(Rs.)", "Ratio");
        System.out.println("  " + "-".repeat(75));

        for (int i = 0; i < n && remaining > 0; i++) {
            int id = idx[i];
            double take = Math.min(weight[id], remaining);
            double val  = take * ratio[id];
            totalValue += val;
            remaining  -= take;
            String loaded = (take == weight[id]) ? "FULL" : String.format("%.2f kg (partial)", take);
            System.out.printf("  %-25s | %-12.2f | %-12.2f | %-10.2f | %s%n",
                names[id], weight[id], value[id], ratio[id], loaded);
        }

        System.out.println("\n--- Summary ---");
        System.out.printf("  Truck Capacity     : %.2f kg%n", capacity);
        System.out.printf("  Weight Loaded      : %.2f kg%n", capacity - remaining);
        System.out.printf("  Total Shipment Value : Rs.%.2f%n", totalValue);
        if (remaining == 0) System.out.println("  Truck is FULLY LOADED. Maximum value achieved.");
        else                System.out.printf("  %.2f kg capacity unused.%n", remaining);
    }
}
