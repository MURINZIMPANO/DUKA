"use client";

import { createContext, useContext, useEffect, useMemo, useState } from "react";
import { createClient, SupabaseClient, Session } from "@supabase/supabase-js";

const SUPABASE_URL = process.env.NEXT_PUBLIC_SUPABASE_URL!;
const SUPABASE_ANON_KEY = process.env.NEXT_PUBLIC_SUPABASE_ANON_KEY!;

type AuthCtx = {
  supabase: SupabaseClient;
  session: Session | null;
  loading: boolean;
  isAdmin: boolean;
  email: string | null;
  signIn: (email: string, password: string) => Promise<string | null>;
  signOut: () => Promise<void>;
};

const Ctx = createContext<AuthCtx | null>(null);

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const supabase = useMemo(
    () => createClient(SUPABASE_URL, SUPABASE_ANON_KEY),
    []
  );
  const [session, setSession] = useState<Session | null>(null);
  const [loading, setLoading] = useState(true);
  const [isAdmin, setIsAdmin] = useState(false);

  useEffect(() => {
    supabase.auth.getSession().then(({ data }) => {
      setSession(data.session);
      setLoading(false);
    });
    const { data: sub } = supabase.auth.onAuthStateChange((_e, s) => {
      setSession(s);
    });
    return () => sub.subscription.unsubscribe();
  }, [supabase]);

  // Allowlist check — admin_users table is the single source of truth.
  useEffect(() => {
    (async () => {
      if (!session?.user?.email) {
        setIsAdmin(false);
        return;
      }
      const email = session.user.email.toLowerCase();
      const { data, error } = await supabase
        .from("admin_users")
        .select("email")
        .eq("email", email)
        .maybeSingle();
      setIsAdmin(!error && !!data);
    })();
  }, [session, supabase]);

  const signIn = async (email: string, password: string) => {
    const { error } = await supabase.auth.signInWithPassword({ email, password });
    return error ? error.message : null;
  };

  const signOut = async () => {
    await supabase.auth.signOut();
    setSession(null);
    setIsAdmin(false);
  };

  return (
    <Ctx.Provider
      value={{
        supabase,
        session,
        loading,
        isAdmin,
        email: session?.user?.email ?? null,
        signIn,
        signOut,
      }}
    >
      {children}
    </Ctx.Provider>
  );
}

export function useAuth(): AuthCtx {
  const ctx = useContext(Ctx);
  if (!ctx) throw new Error("useAuth must be used inside AuthProvider");
  return ctx;
}
