import { useEffect, useRef } from 'react';
import 'ol/ol.css';
import Map from 'ol/Map';
import View from 'ol/View';
import TileLayer from 'ol/layer/Tile';
import VectorLayer from 'ol/layer/Vector';
import OSM from 'ol/source/OSM';
import VectorSource from 'ol/source/Vector';
import Feature from 'ol/Feature';
import Point from 'ol/geom/Point';
import { fromLonLat } from 'ol/proj';
import { styleForStation } from '../utils/stationStyles';

// Warszawa, mniej więcej Centrum
const WARSAW_CENTER = [21.0122, 52.2297]; // [lon, lat]
const DEFAULT_ZOOM = 12;

export default function MapView({
  stations,
  selectedStationId,
  onStationClick,
  onStationHover,
}) {
  const containerRef = useRef(null);
  const mapRef = useRef(null);
  const sourceRef = useRef(null);
  const hoveredFeatureRef = useRef(null);

  useEffect(() => {
    if (mapRef.current) return;

    const source = new VectorSource();
    sourceRef.current = source;

    const map = new Map({
      target: containerRef.current,
      controls: [],
      layers: [
        new TileLayer({ source: new OSM() }),
        new VectorLayer({ source, declutter: false }),
      ],
      view: new View({
        center: fromLonLat(WARSAW_CENTER),
        zoom: DEFAULT_ZOOM,
        minZoom: 10,
        maxZoom: 19,
      }),
    });
    mapRef.current = map;

    map.on('pointermove', (evt) => {
      if (evt.dragging) {
        onStationHover(null);
        return;
      }
      const feature = map.forEachFeatureAtPixel(evt.pixel, (f) => f, {
        hitTolerance: 4,
      });
      const target = map.getTargetElement();
      target.style.cursor = feature ? 'pointer' : '';

      if (hoveredFeatureRef.current && hoveredFeatureRef.current !== feature) {
        const prev = hoveredFeatureRef.current;
        const isSelected = prev.get('station').stationId === selectedStationIdRef.current;
        prev.setStyle(styleForStation(prev.get('station'), { selected: isSelected }));
      }

      if (feature) {
        const station = feature.get('station');
        const isSelected = station.stationId === selectedStationIdRef.current;
        feature.setStyle(styleForStation(station, { hovered: !isSelected, selected: isSelected }));
        hoveredFeatureRef.current = feature;

        onStationHover({
          station,
          x: evt.originalEvent.clientX,
          y: evt.originalEvent.clientY,
        });
      } else {
        hoveredFeatureRef.current = null;
        onStationHover(null);
      }
    });

    map.on('click', (evt) => {
      const feature = map.forEachFeatureAtPixel(evt.pixel, (f) => f, {
        hitTolerance: 4,
      });
      if (feature) {
        onStationClick(feature.get('station'));
      } else {
        onStationClick(null); // klik w pustą część mapy zamyka panel
      }
    });

    return () => {
      map.setTarget(undefined);
      mapRef.current = null;
    };
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);


  const selectedStationIdRef = useRef(selectedStationId);
  useEffect(() => {
    selectedStationIdRef.current = selectedStationId;
  }, [selectedStationId]);

  useEffect(() => {
    const source = sourceRef.current;
    if (!source) return;

    source.clear();
    const features = stations.map((s) => {
      const f = new Feature({
        geometry: new Point(fromLonLat([s.lon, s.lat])),
        station: s,
      });
      f.setId(s.stationId);
      f.setStyle(
        styleForStation(s, { selected: s.stationId === selectedStationId })
      );
      return f;
    });
    source.addFeatures(features);
  }, [stations, selectedStationId]);

  return <div ref={containerRef} className="map-container" />;
}
