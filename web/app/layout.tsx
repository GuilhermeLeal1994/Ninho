import type { Metadata } from "next";
import "./globals.css";
export const metadata: Metadata = { title: "Ninho | Conforto em casa", description: "Painel de conforto ambiental do Ninho" };
export default function RootLayout({children}:{children:React.ReactNode}) {return <html lang="pt-BR"><body>{children}</body></html>}
