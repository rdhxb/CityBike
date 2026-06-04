**Practice Accelerator Program**

Software Engineer Intern

*Home Task (Practice Edition)*

## 🚲 Home Task: City Bike Station Explorer

**Context:** You're building the foundation of an urban mobility dashboard. The goal is to collect real, live data about public bike-sharing stations in a city and present it in a simple, usable web application — from data collection all the way to the browser.

> This is a practice task, modeled on a real full-stack home assignment. Different domain, different data source, same shape and difficulty. Treat it like the real thing: timebox it, write a README, and be ready to defend your choices out loud.

---

### 🎯 What to Build

This is a full-stack task with three parts: data collection, backend API, and frontend UI.
As a result, it should be a single fully working application end-to-end.

---

### Part 1 — Data Collection

Collect data on at least **100 bike-sharing stations** in one city from any publicly available source.

**Recommended source — GBFS (General Bikeshare Feed Specification):** an open, no-key, JSON-based standard used by hundreds of systems worldwide (Veturilo/Nextbike, Citi Bike, Bicing, Vélib', etc.). It is the bikesharing equivalent of an open public API.

- Discovery endpoint that lists every public GBFS system: `https://github.com/MobilityData/gbfs/blob/master/systems.csv` (each row points to a system's `gbfs.json` auto-discovery file).
- A typical system exposes `station_information.json` (static: name, location, capacity) and `station_status.json` (live: bikes available, docks available). You'll likely want to **join these two feeds on `station_id`**.
- Free choice of system. Pick any city with ≥100 stations.

Other sources are allowed (Overpass/OpenStreetMap `amenity=bicycle_rental`, a city open-data portal, etc.) — your creativity is welcomed.

For each station, capture as many fields as possible:

| Field | Required? |
| ----- | ----- |
| Station ID | ✅ |
| Station name | ✅ |
| Latitude / longitude | ✅ |
| Capacity (total docks) | ⭐ nice to have |
| Bikes available now | ⭐ nice to have |
| Docks available now | ⭐ nice to have |
| Is the station active / renting / returning | ⭐ nice to have |
| Address or neighborhood | ⭐ nice to have |
| Last reported / updated timestamp | ⭐ nice to have |

**Rules:**

- Store the data in any format: SQLite, JSON, CSV, or a database of your choice.
- The two feeds are separate — be deliberate about how you merge static info with live status.

---

### Part 2 — Backend API

Expose the collected data via a simple REST API:

- return the full list of stations (id, name, neighborhood, capacity, bikes available)
- return full details for a single station
- allow modification of a single station's details (e.g. correcting a name, marking a station inactive)

---

### Part 3 — Frontend UI

A simple web interface that consumes the API:

- A **listing page** — browse all stations, see key info at a glance (name, neighborhood, capacity, bikes available)
- Basic **search or filter** — by neighborhood or by availability, e.g. "only stations with bikes available" (at minimum one of these)
- A **detail view** — click a station to see full details (coordinates, capacity, live availability, last updated, etc.). A small map pin is a nice touch but not required.
- Manual **modification** of details and saving changes to backend storage via the API

---

### 📁 Deliverable

A link to a publicly available GitHub repository (preferable) or an uploaded archive containing:

- Application code
- A README.md explaining:
  - How to run the application
  - Technical solution and frameworks/tools used
  - What you'd improve with more time

---

### 🔧 Technology

Free choice — any language, framework, or tooling. **Kotlin (or Java) + Spring on the backend and React / Next.js on the frontend are a plus**, but not required.

---

### ✅ What We Care About

- **Data quality** — accurate, consistent, deduplicated records; sensible handling of the join between static info and live status
- **Code quality** — readable, structured, maintainable
- **Product thinking** — does the UI actually help someone find a bike right now?
- **Documentation** — can someone else run this from scratch?

We care more about how you think and how you work than whether you hit 100 records exactly.

### ⏱️ Time Expectation

**~4–8 hours.**

### ❓ Be ready to discuss

- Demo the app — show it running with real data
- Why did you choose your data source / which GBFS system, and why?
- How did you handle missing, stale, or inconsistent data (e.g. a station in `station_status` with no matching `station_information`)?
- Live data changes constantly — how would you keep it fresh? (polling interval, caching, refresh strategy)
- How would you scale this to cover **every** bike-sharing system in the country (or in GBFS worldwide)?

---

### 💡 Hints (only if you get stuck — try without them first)

- Start by fetching one system's `gbfs.json`, read the `feeds` array, then fetch `station_information` and `station_status` from there. Don't hardcode sub-feed URLs.
- The natural data model is one `Station` object with static fields + a nested or flattened `status`. Join on `station_id`.
- For the "modify a station" endpoint, decide up front: do edits persist across a data re-fetch, or does re-fetching overwrite them? Be ready to explain that decision.
- A neighborhood field often isn't in the feed. Reverse-geocoding is optional; deriving a rough area from coordinates, or just leaving it nullable, is a fine documented tradeoff.

### 🧭 Suggested approach

1. **Spike the data (1–2h):** fetch, join the two feeds, dump ~100 stations to local storage. Confirm the data looks right before writing any API.
2. **Backend (1–2h):** read from storage, expose the three endpoints, add the modify endpoint last.
3. **Frontend (1–2h):** list → filter → detail → edit. Get the list rendering before polishing.
4. **README + cleanup (~1h):** run instructions, choices, "what I'd improve."
