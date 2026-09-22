import { createClient } from "@supabase/supabase-js";

export const supabaseServer = createClient(
  process.env.NEXT_PUBLIC_SUPABASE_URL!,
  process.env.NEXT_PUBLIC_SUPABASE_ANON_KEY!
);

export type Shop = {
  id: string;
  name: string;
  category: string;
  district: string;
  cover_url: string;
  avg_price: number | string | null;
  sales_velocity: number | string | null;
  product_count: number;
  registered_at: string;
  rating_average: number | string | null;
  rating_count: number;
  is_flagged: boolean;
};

export type ShopProduct = {
  id: number;
  shop_id: string;
  name: string;
  price: number | string | null;
  category: string;
};

/** ₣ / ₣₣ / ₣₣₣ rough price-range indicator (mirrors the mobile app). */
export function priceRangeSymbol(avgPrice: number | string | null): string {
  const p = Number(avgPrice ?? 0);
  if (p <= 0) return "";
  if (p < 1000) return "₣";
  if (p < 5000) return "₣₣";
  return "₣₣₣";
}
