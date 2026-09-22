"use client";

import { use, useEffect, useState } from "react";
import { useAuth } from "../../../lib/auth";
import { Shop, ShopProduct, priceRangeSymbol } from "../../../lib/supabase";

export default function ShopDetailPage({
  params,
}: {
  params: Promise<{ id: string }>;
}) {
  const { id } = use(params);
  const { supabase, session, isAdmin, loading: authLoading } = useAuth();
  const [shop, setShop] = useState<Shop | null>(null);
  const [products, setProducts] = useState<ShopProduct[]>([]);
  const [loading, setLoading] = useState(true);
  const [busy, setBusy] = useState(false);

  useEffect(() => {
    if (!isAdmin) return;
    (async () => {
      const [{ data: shop }, { data: products }] = await Promise.all([
        supabase.from("shops").select("*").eq("id", id).maybeSingle(),
        supabase.from("shop_products").select("*").eq("shop_id", id).order("name"),
      ]);
      setShop((shop as Shop) ?? null);
      setProducts((products as ShopProduct[]) ?? []);
      setLoading(false);
    })();
  }, [supabase, id, isAdmin]);

  const toggleFlag = async () => {
    if (!shop) return;
    setBusy(true);
    const { data } = await supabase
      .from("shops")
      .update({ is_flagged: !shop.is_flagged })
      .eq("id", id)
      .select()
      .maybeSingle();
    if (data) setShop(data as Shop);
    setBusy(false);
  };

  if (authLoading) return <main style={{ padding: 40 }}>Loading…</main>;
  if (!session || !isAdmin)
    return (
      <main style={{ padding: 40 }}>
        <a href="/">← Back to sign-in</a>
      </main>
    );
  if (loading) return <main style={{ padding: 40 }}>Loading shop…</main>;
  if (!shop)
    return (
      <main style={{ padding: 40 }}>
        <a href="/">← Back</a>
        <h1>Shop not found</h1>
      </main>
    );

  return (
    <main style={{ maxWidth: 900, margin: "0 auto", padding: 24 }}>
      <a href="/">← All shops</a>
      <header
        style={{
          display: "flex",
          justifyContent: "space-between",
          alignItems: "flex-start",
          margin: "14px 0 20px",
        }}
      >
        <div>
          <h1 style={{ margin: "0 0 4px" }}>{shop.name}</h1>
          <div className="muted">
            {shop.category} · {shop.district || "—"} · Registered{" "}
            {new Date(shop.registered_at).toLocaleDateString()}
          </div>
          <div style={{ marginTop: 8 }}>
            <span className={`badge ${shop.is_flagged ? "flagged" : "active"}`}>
              {shop.is_flagged ? "Flagged" : "Active"}
            </span>{" "}
            <span className="muted">
              {shop.rating_count > 0
                ? `★ ${Number(shop.rating_average).toFixed(1)} (${shop.rating_count} ratings)`
                : "No ratings yet"}
              {` · ${priceRangeSymbol(shop.avg_price)} avg ₣${Number(shop.avg_price ?? 0).toFixed(0)}`}
              {` · ${shop.product_count} products`}
            </span>
          </div>
        </div>
        <button
          className={`button ${shop.is_flagged ? "secondary" : "danger"}`}
          onClick={toggleFlag}
          disabled={busy}
        >
          {busy ? "Saving…" : shop.is_flagged ? "Unflag shop" : "Flag shop"}
        </button>
      </header>

      <section className="card" style={{ marginBottom: 20 }}>
        <h2 style={{ margin: "0 0 6px", fontSize: 16 }}>Public profile</h2>
        <p className="muted" style={{ marginTop: 0 }}>
          This is exactly what a client sees for this shop on Explore right now.
        </p>
        <div style={{ display: "flex", gap: 18, flexWrap: "wrap" }}>
          <Detail label="Name" value={shop.name} />
          <Detail label="Category" value={shop.category} />
          <Detail label="District" value={shop.district || "—"} />
          <Detail
            label="Price range"
            value={priceRangeSymbol(shop.avg_price) || "—"}
          />
          <Detail
            label="Sales velocity (agg.)"
            value={`${Number(shop.sales_velocity ?? 0).toFixed(2)}/day`}
          />
          <Detail label="Cover image" value={shop.cover_url ? "uploaded" : "placeholder"} />
        </div>
      </section>

      <section>
        <h2 style={{ fontSize: 16 }}>Product catalog ({products.length})</h2>
        {products.length === 0 ? (
          <p className="muted">
            No public products synced yet — the shop may not have synced since
            adding products.
          </p>
        ) : (
          <table>
            <thead>
              <tr>
                <th>Product</th>
                <th>Category</th>
                <th>Price (₣)</th>
              </tr>
            </thead>
            <tbody>
              {products.map((p) => (
                <tr key={p.id}>
                  <td>{p.name}</td>
                  <td>{p.category || "—"}</td>
                  <td>{Number(p.price ?? 0).toFixed(0)}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </section>
    </main>
  );
}

function Detail({ label, value }: { label: string; value: string }) {
  return (
    <div>
      <div className="muted" style={{ fontSize: 11, textTransform: "uppercase" }}>
        {label}
      </div>
      <div style={{ fontWeight: 600 }}>{value}</div>
    </div>
  );
}
