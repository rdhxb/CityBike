import { useState } from 'react';
import MapView from './components/MapView';
import StationDetailsPanel from './components/StationDetailsPanel';
import StationTooltip from './components/StationTooltip';
import { useStations } from './hooks/useStations';

export default function App() {
  const { stations, loading, error, lastUpdated } = useStations();
  const [selected, setSelected] = useState(null);
  const [hoverInfo, setHoverInfo] = useState(null);

  const availableCount = stations.filter(
    (s) => s.isRenting && s.numBikesAvailable > 0
  ).length;

  return (
    <div className="app">
      <header className="topbar">
        <div className="topbar-brand">
          <span className="topbar-mark">●</span>
          <span className="topbar-name">VETURILO</span>
          <span className="topbar-subtitle">mapa stacji · Warszawa</span>
        </div>

        <div className="topbar-stats">
          {loading && <span className="status status-loading">Ładowanie…</span>}
          {error && (
            <span className="status status-error">
              Błąd połączenia: {error}
            </span>
          )}
          {!loading && !error && (
            <>
              <Metric label="Stacji" value={stations.length} />
              <Metric label="Dostępnych" value={availableCount} accent />
              <Metric
                label="Aktualizacja"
                value={
                  lastUpdated
                    ? lastUpdated.toLocaleTimeString('pl-PL', {
                        hour: '2-digit',
                        minute: '2-digit',
                        second: '2-digit',
                      })
                    : '—'
                }
              />
            </>
          )}
        </div>
      </header>

      <main className="main">
        <MapView
          stations={stations}
          selectedStationId={selected?.stationId}
          onStationClick={setSelected}
          onStationHover={setHoverInfo}
        />
        <StationDetailsPanel
          station={selected}
          onClose={() => setSelected(null)}
        />
      </main>

      <StationTooltip hoverInfo={hoverInfo} />
    </div>
  );
}

function Metric({ label, value, accent = false }) {
  return (
    <div className={`metric ${accent ? 'metric-accent' : ''}`}>
      <span className="metric-label">{label}</span>
      <span className="metric-value">{value}</span>
    </div>
  );
}
