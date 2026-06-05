import { useEffect, useRef, useState } from 'react';

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080';
const REFRESH_INTERVAL_MS = 60_000;

export function useStations() {
  const [stations, setStations] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [lastUpdated, setLastUpdated] = useState(null);
  const abortRef = useRef(null);

  useEffect(() => {
    let cancelled = false;

    async function fetchStations() {
      if (abortRef.current) abortRef.current.abort();
      const controller = new AbortController();
      abortRef.current = controller;

      try {
        const res = await fetch(`${API_URL}/api/stations`, {
          signal: controller.signal,
        });
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        const data = await res.json();
        if (cancelled) return;
        setStations(data);
        setError(null);
        setLastUpdated(new Date());
      } catch (e) {
        if (e.name === 'AbortError' || cancelled) return;
        setError(e.message);
      } finally {
        if (!cancelled) setLoading(false);
      }
    }

    fetchStations();
    const intervalId = setInterval(fetchStations, REFRESH_INTERVAL_MS);

    return () => {
      cancelled = true;
      clearInterval(intervalId);
      if (abortRef.current) abortRef.current.abort();
    };
  }, []);

  return { stations, loading, error, lastUpdated };
}
