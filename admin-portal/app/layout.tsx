import type { Metadata } from "next";
import "../styles/globals.css";
import { AuthProvider } from "../lib/auth";

export const metadata: Metadata = {
  title: "Duka Admin",
  description: "Internal ops portal for the Duka platform",
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="en">
      <body>
        <AuthProvider>{children}</AuthProvider>
      </body>
    </html>
  );
}
