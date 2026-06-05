import { stationStatus, stationLabel } from '../utils/stationStyles';

export default function StationTooltip({ hoverInfo }) {
  if (!hoverInfo) return null;

  const { station, x, y } = hoverInfo;
  const status = stationStatus(station);

  // Trzymaj tooltip w obrębie viewportu (proste przesunięcie offsetem,
  // bardziej zaawansowane flipowanie pomijam)
  const style = {
    left: x + 14,
    top: y + 14,
  };

  return (
    <div className="tooltip" style={style} role="tooltip">
      <div className="tooltip-name">{station.name}</div>
      <div className="tooltip-row">
        <span className={`dot dot-${status}`} aria-hidden />
        <span>{stationLabel(status)}</span>
      </div>
      <div className="tooltip-meta">
        {station.isRenting === true ? '✓ wypożycza ' : '✗ nie wypożycza'}
        <span className="tooltip-sep">·</span>
        {station.isReturning === true ? '✓ przyjmuje zwroty' : '✗ nie przyjmuje zwrotów'}
      </div>
    </div>
  );
}
