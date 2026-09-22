"use client";

import { useEffect, useMemo, useState } from "react";
import { useAuth } from "../lib/auth";
import { Shop, priceRangeSymbol } from "../lib/supabase";

export default function Home() {
  const { session, loading, isAdmin, email, signIn, signOut } = useAuth();

  if (loading) {
    return <main style={{ padding: 40 }}>Loading…</main>;
  }

  if (!session) {
    return <LoginScreen onSignIn={signIn} />;
  }

  if (!isAdmin) {
    return (
      <main style={{ padding: 40 }}>
        <h1>Not an admin</h1>
        <p className="muted">
          {email} is signed in but is not on the platform admin allowlist
          (<code>admin_users</code> table). Ask an existing admin to add you.
        </p>
        <button className="button secondary" onClick={() => signOut()}>
          Sign out
        </button>
      </main>
    );
  }

  return <Dashboard email={email!} onSignOut={signOut} />;
}

function LoginScreen({
  onSignIn,
}: {
  onSignIn: (email: string, password: string) => Promise<string | null>;
}) {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState<string | null>(null);
  const [busy, setBusy] = useState(false);

  return (
    <main
      style={{
        maxWidth: 360,
        margin: "80px auto",
        padding: 24,
      }}
    >
      <div className="card">
        <h1 style={{ marginTop: 0 }}>Duka Admin</h1>
        <p className="muted">Platform administrator sign-in.</p>
        <form
          onSubmit={async (e) => {
            e.preventDefault();
            setBusy(true);
            setError(await onSignIn(email, password));
            setBusy(false);
          }}
        >
          <input
            className="input"
            type="email"
            placeholder="Admin email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            style={{ marginBottom: 10 }}
            required
          />
          <input
            className="input"
            type="password"
            placeholder="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            style={{ marginBottom: 14 }}
            required
          />
          {error && (
            <p style={{ color: "var(--clay)", fontSize: 13 }}>{error}</p>
          )}
          <button className="button" disabled={busy} style={{ width: "100%" }}>
            {busy ? "Signing in…" : "Sign in"}
          </button>
        </form>
      </div>
    </main>
  );
}

function Dashboard({
  email,
  onSignOut,
}: {
  email: string;
  onSignOut: () => void;
}) {
  const { supabase } = useAuth();
  const [shops, setShops] = useState<Shop[]>([]);
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState("");

  useEffect(() => {
    (async () => {
      const { data } = await supabase
        .from("shops")
        .select("*")
        .order("registered_at", { ascending: false })
        .limit(1000);
      setShops((data as Shop[]) ?? []);
      setLoading(false);
    })();
  }, [supabase]);

  const filtered = useMemo(() => {
    const q = search.trim().toLowerCase();
    if (!q) return shops;
    return shops.filter(
      (s) =>
        s.name.toLowerCase().includes(q) ||
        s.district.toLowerCase().includes(q) ||
        s.category.toLowerCase().includes(q)
    );
  }, [shops, search]);

  return (
    <main style={{ maxWidth: 1100, margin: "0 auto", padding: 24 }}>
      <header
        style={{
          display: "flex",
          justifyContent: "space-between",
          alignItems: "center",
          marginBottom: 20,
        }}
      >
        <div>
          <h1 style={{ margin: 0 }}>Duka Admin</h1>
          <span className="muted">Signed in as {email}</span>
        </div>
        <button className="button secondary" onClick={onSignOut}>
          Sign out
        </button>
      </header>

      <Stats shops={shops} />

      <section style={{ marginTop: 28 }}>
        <div
          style={{
            display: "flex",
            justifyContent: "space-between",
            alignItems: "center",
            marginBottom: 10,
          }}
        >
          <h2 style={{ margin: 0 }}>Shops ({filtered.length})</h2>
          <input
            className="input"
            style={{ maxWidth: 280 }}
            placeholder="Search shops…"
            value={search}
            onChange={(e) => setSearch(e.target.value)}
          />
        </div>
        {loading ? (
          <p className="muted">Loading shops…</p>
        ) : (
          <ShopTable shops={filtered} />
        )}
      </section>
    </main>
  );
}

function Stats({ shops }: { shops: Shop[] }) {
  const byCategory = useMemo(() => {
    const m = new Map<string, number>();
    shops.forEach((s) => m.set(s.category, (m.get(s.category) ?? 0) + 1));
    return [...m.entries()].sort((a, b) => b[1] - a[1]);
  }, [shops]);

  const byDistrict = useMemo(() => {
    const m = new Map<string, number>();
    shops.forEach((s) => m.set(s.district || "—", (m.get(s.district || "—") ?? 0) + 1));
    return [...m.entries()].sort((a, b) => b[1] - a[1]);
  }, [shops]);

  const now = Date.now();
  const week = 7 * 24 * 3600 * 1000;
  const month = 30 * 24 * 3600 * 1000;
  const newThisWeek = shops.filter(
    (s) => now - new Date(s.registered_at).getTime() < week
  ).length;
  const newThisMonth = shops.filter(
    (s) => now - new Date(s.registered_at).getTime() < month
  ).length;

  return (
    <section className="stat-grid">
      <div className="card">
        <div className="muted">Total shops</div>
        <div className="stat-number">{shops.length}</div>
      </div>
      <div className="card">
        <div className="muted">New this week</div>
        <div className="stat-number">{newThisWeek}</div>
      </div>
      <div className="card">
        <div className="muted">New this month</div>
        <div className="stat-number">{newThisMonth}</div>
      </div>
      <div className="card">
        <div className="muted">By category</div>
        {byCategory.length === 0 && <div className="muted">—</div>}
        {byCategory.map(([c, n]) => (
          <div key={c} style={{ fontSize: 13 }}>
            {c}: <b>{n}</b>
          </div>
        ))}
      </div>
      <div className="card">
        <div className="muted">By district</div>
        {byDistrict.length === 0 && <div className="muted">—</div>}
        {byDistrict.slice(0, 6).map(([d, n]) => (
          <div key={d} style={{ fontSize: 13 }}>
            {d}: <b>{n}</b>
          </div>
        ))}
      </div>
    </section>
  );
}

function ShopTable({ shops }: { shops: Shop[] }) {
  if (shops.length === 0) return <p className="muted">No shops registered yet.</p>;
  return (
    <table>
      <thead>
        <tr>
          <th>Shop</th>
          <th>Category</th>
          <th>District</th>
          <th>Rating</th>
          <th>Price</th>
          <th>Products</th>
          <th>Registered</th>
          <th>Status</th>
          <th></th>
        </tr>
      </thead>
      <tbody>
        {shops.map((s) => (
          <tr key={s.id}>
            <td>
              <a href={`/shops/${encodeURIComponent(s.id)}`}>{s.name}</a>
            </td>
            <td>{s.category}</td>
            <td>{s.district}</td>
            <td>
              {s.rating_count > 0
                ? `${Number(s.rating_average).toFixed(1)} (${s.rating_count})`
                : "—"}
            </td>
            <td>{priceRangeSymbol(s.avg_price) || "—"}</td>
            <td>{s.product_count}</td>
            <td>{new Date(s.registered_at).toLocaleDateString()}</td>
            <td>
              <span className={`badge ${s.is_flagged ? "flagged" : "active"}`}>
                {s.is_flagged ? "Flagged" : "Active"}
              </span>
            </td>
            <td>
              <a href={`/shops/${encodeURIComponent(s.id)}`}>View</a>
            </td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}
