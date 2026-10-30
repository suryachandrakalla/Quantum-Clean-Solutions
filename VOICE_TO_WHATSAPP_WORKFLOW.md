# Voice-to-WhatsApp Ordering Engine: Technical Logic Flow & Webhook Pipeline

This technical specification details the complete sequence for **Quantum Clean Solutions**, converting natural voice speech into structured orders, deducting Quantum Cash wallet balances, and triggering WhatsApp Business API messages with automated logistics allocation.

---

## 1. High-Level Pipeline Architecture

```
[Customer Speaks] (Natural Hindi/English Audio)
       │
       ▼
[1. Speech-to-Text Layer] ──► Whisper / Gemini 3.5 Transcribe
       │                      Outputs raw text: "Send 2 cans of 5L Stainoff and 1 box of dishwash to Indiranagar"
       ▼
[2. Intent & Entity Extraction] ──► Gemini 3.8 / 3.1 Flash with JSON Schema Output
       │                            Extracts: SKUs, quantities, packaging size, destination pincode
       ▼
[3. Pricing & Wallet Calculation] ──► Next.js / Edge Worker
       │                              • Queries Products & Bulk Tiers
       │                              • Deducts available Quantum Cash
       │                              • Calculates competitor savings
       ▼
[4. Supabase DB Persistence] ──► Inserts `orders` & `order_items` (Status: 'draft' / 'placed')
       │                         Executes `find_and_route_order_dealer` (Dynamic PostGIS Matrix)
       ▼
[5. Webhook Dispatch to n8n / Meta API] ──► WhatsApp Business Cloud API
       │
       ├─► Customer WhatsApp: Interactive Button Message (UPI Link + COD Confirmation)
       └─► Assigned Dealer WhatsApp: New Dispatch Alert with 1-Click Accept
```

---

## 2. Step-by-Step Technical Execution

### Step 1: Audio Ingestion & Transcription (Whisper / Gemini Transcribe)
- **Input**: Raw PCM / AAC audio buffer from the mobile app (or WhatsApp voice note `.ogg`).
- **Endpoint**: `POST /api/voice/transcribe`
- **Model Call**:
  ```typescript
  const transcript = await ai.transcribeAudio({
    model: "gemini-3.5-transcribe",
    audioBuffer: req.body.audio,
    languagePrompt: "Cleaning chemicals, detergents, liters, canisters, barrels, Quantum SKUs."
  });
  ```

---

### Step 2: LLM Intent Parsing with Structured Output (JSON Schema)
The raw transcript is supplied to the LLM with a system instruction and strict JSON schema constraining output to verified Quantum catalog SKUs.

**System Prompt**:
```
You are the Quantum Clean Solutions Automated Ordering Assistant.
Convert natural language voice transcriptions into structured cart items.
Always map product mentions to canonical Quantum SKUs:
- "Stainoff" / "Detergent" -> Q-STAINOFF-LAV-5L or Q-STAINOFF-ENZ-2L
- "Swipe Shine" / "Dishwash" -> Q-SWIPESHINE-5L
- "Flush Max" / "Toilet cleaner" -> Q-FLUSHMAX-5L
- "Descale Pro" / "Limescale" -> Q-DESCALEL-5L
- "Hygienix" / "Hand wash" -> Q-HYGIENIX-5L
- "Aromist" / "Air freshener" -> Q-AROMIST-5L
- "Degreaser" / "Alkaline barrel" -> Q-DEGREASE-200L
Default size to '5L' if unspecified. Default quantity to 1 if unspecified.
Extract delivery address or destination pincode if mentioned.
```

**JSON Output Schema**:
```json
{
  "type": "object",
  "properties": {
    "intent": { "type": "string", "enum": ["create_order", "inquire_price", "track_order"] },
    "items": {
      "type": "array",
      "items": {
        "type": "object",
        "properties": {
          "sku": { "type": "string" },
          "quantity": { "type": "integer", "minimum": 1 },
          "requested_size": { "type": "string" }
        },
        "required": ["sku", "quantity"]
      }
    },
    "delivery_hint": { "type": "string" },
    "pincode_hint": { "type": "string" }
  },
  "required": ["intent", "items"]
}
```

---

### Step 3: Server-Side Cart Calculation & Quantum Cash Deduction

The backend checks database prices and the customer's wallet balance:

```typescript
// 1. Fetch SKUs from Supabase
const { data: products } = await supabase
  .from('products')
  .select('id, sku, name, b2c_price, competitor_price, cashback_quantum_cash, weight_kg')
  .in('sku', extractedSkus);

// 2. Compute Totals
let totalMrp = 0;
let competitorTotal = 0;
let totalWeightKg = 0;
let earnedCash = 0;

for (const item of parsedResult.items) {
  const prod = products.find(p => p.sku === item.sku);
  totalMrp += prod.b2c_price * item.quantity;
  competitorTotal += prod.competitor_price * item.quantity;
  totalWeightKg += prod.weight_kg * item.quantity;
  earnedCash += prod.cashback_quantum_cash * item.quantity;
}

// 3. Apply Quantum Cash Wallet Deduction
const { data: user } = await supabase.from('users').select('wallet_balance').eq('id', userId).single();
const cashDeduction = Math.min(user.wallet_balance, totalMrp);
const finalPayable = totalMrp - cashDeduction;
const competitorSavings = competitorTotal - totalMrp;
```

---

### Step 4: Supabase Database Transaction & Dynamic Geo-Routing

All actions execute within a single transaction or stored procedure:

```sql
-- 1. Insert Order
INSERT INTO orders (
    id, order_number, customer_id, total_mrp, quantum_cash_applied, 
    final_payable, earned_quantum_cash, delivery_address, delivery_city, 
    delivery_pincode, channel, status
) VALUES (
    v_order_id, 'Q-ORD-9842', v_user_id, 1550.00, 200.00,
    1350.00, 115.00, 'Hotel Grand Horizon, Indiranagar', 'Bengaluru',
    '560038', 'ai_voice', 'placed'
);

-- 2. Insert Order Items
INSERT INTO order_items (order_id, product_id, quantity, unit_price, total_price)
VALUES 
    (v_order_id, 'prod-stainoff-5l', 2, 499.00, 998.00),
    (v_order_id, 'prod-swipeshine-5l', 1, 380.00, 380.00);

-- 3. Execute PostGIS Dynamic Radius Routing
SELECT find_and_route_order_dealer(v_order_id, 12.9716, 77.5946);
```

---

### Step 5: Webhook Dispatch to WhatsApp Business Cloud API (via n8n)

An event is posted to the n8n automation webhook (`POST https://n8n.quantumclean.in/webhook/voice-order-placed`), which formats the WhatsApp Interactive Template:

#### WhatsApp Interactive Message Payload:
```json
{
  "messaging_product": "whatsapp",
  "recipient_type": "individual",
  "to": "+919885031431",
  "type": "interactive",
  "interactive": {
    "type": "button",
    "header": {
      "type": "text",
      "text": "🧼 Quantum Clean Solutions — Order #Q-ORD-9842"
    },
    "body": {
      "text": "Hi Rajesh! Your Voice AI Order has been confirmed and routed to *Koramangala Station (3.2 km)*:\n\n• 2x STAINOFF Liquid Detergent 5L: ₹998\n• 1x SWIPE SHINE Dish Wash 5L: ₹380\n─────────────────────\n*Total MRP:* ₹1,378\n*Quantum Cash Deducted:* -₹200.00\n*Net Payable:* *₹1,178.00*\n*Competitor Savings:* ₹582.00 saved vs leading brand!\n*Reward:* You earn +₹115 Quantum Cash upon delivery.\n\n*Assigned Carrier:* Dunzo Express (< 30kg)\n\nPlease choose your payment method below:"
    },
    "footer": {
      "text": "ISO 9001 Certified • Direct Factory Delivery"
    },
    "action": {
      "buttons": [
        {
          "type": "reply",
          "reply": {
            "id": "PAY_UPI_INSTANT",
            "title": "⚡ Pay via UPI (5% Off)"
          }
        },
        {
          "type": "reply",
          "reply": {
            "id": "CONFIRM_COD",
            "title": "💵 Cash on Delivery"
          }
        }
      ]
    }
  }
}
```

---

### Step 6: Payment Webhook Callback & State Update

When the user taps **"⚡ Pay via UPI"** or completes payment via Razorpay UPI:
1. Meta sends a `messages` webhook callback to the backend.
2. The order status updates:
   ```sql
   UPDATE orders SET status = 'confirmed', updated_at = NOW() WHERE order_number = 'Q-ORD-9842';
   ```
3. Automatic Carrier Booking Webhook triggers:
   - Total weight = $15.6\text{ kg} < 30\text{ kg}$ $\longrightarrow$ **Dunzo API** receives pickup task from dealer address.
   - If total weight was $\ge 30\text{ kg}$ $\longrightarrow$ **Porter API** assigns a 3-Wheeler Mini Truck.
4. WhatsApp pushes real-time tracking link directly into the customer's chat thread.
