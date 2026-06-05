import { stationStatus, stationLabel } from '../utils/stationStyles';

export default function StationDetailsPanel({ station, onClose }) {
  if (!station) return null;

  const status = stationStatus(station);
  const occupancy =
    station.capacity > 0
      ? Math.round((station.numBikesAvailable / station.capacity) * 100)
      : 0;

  return (
    <aside className="panel" aria-label="Szczegóły stacji">
      <button
        className="panel-close"
        onClick={onClose}
        aria-label="Zamknij panel"
      >
        ×
      </button>

      <div className="panel-eyebrow">
        <span className={`dot dot-${status}`} aria-hidden />
        <span>{stationLabel(status)}</span>
        <span className="panel-eyebrow-sep">/</span>
        <span className="panel-id">#{station.stationId}</span>
      </div>

      <h2 className="panel-title">{station.name}</h2>

      <div className="panel-stats">
        <Stat label="Rowery" value={station.numBikesAvailable} accent />
        <Stat label="Wolne doki" value={station.numDocksAvailable} />
        <Stat label="Pojemność" value={station.capacity} />
        <Stat label="Zapełnienie" value={`${occupancy}%`} />
      </div>

      <div className="panel-capacity-bar" aria-label="Wykorzystanie pojemności">
        <div
          className="panel-capacity-fill"
          style={{ width: `${Math.min(occupancy, 100)}%` }}
        />
        <div className="panel-capacity-meta">
          <span>0</span>
          <span>{station.capacity}</span>
        </div>
      </div>

      <dl className="panel-flags">
        <Flag label="Wypożycza rowery" value={station.isRenting} />
        <Flag label="Przyjmuje zwroty" value={station.isReturning} />
      </dl>

      <div className="panel-coords">
        <span className="panel-coords-label">Współrzędne</span>
        <span className="panel-coords-value">
          {station.lat.toFixed(5)}, {station.lon.toFixed(5)}
        </span>
      </div>
    </aside>
  );
}

function Stat({ label, value, accent = false }) {
  return (
    <div className={`stat ${accent ? 'stat-accent' : ''}`}>
      <div className="stat-label">{label}</div>
      <div className="stat-value">{value}</div>
    </div>
  );
}

function Flag({ label, value }) {
  return (
    <div className={`flag ${value ? 'flag-on' : 'flag-off'}`}>
      <dt>{label}</dt>
      <dd>{value ? 'TAK' : 'NIE'}</dd>
    </div>
  );
}
