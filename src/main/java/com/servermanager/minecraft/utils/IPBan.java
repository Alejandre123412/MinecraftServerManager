package com.servermanager.minecraft.utils;

import java.util.Objects;

/**
 * Representa una entrada de IP baneada,
 * con información sobre la IP, razón, fecha de creación,
 * fuente y expiración del baneo.
 */
public class IPBan {

    /** Dirección IP baneada */
    public String ip;

    /** Razón del baneo */
    public String reason;

    /** Fecha de creación del baneo */
    public String created;

    /** Fuente que aplicó el baneo */
    public String source;

    /** Fecha de expiración del baneo */
    public String expires;

    @Override
    public String toString() {
        return ip + (reason != null ? " (Reason: " + reason + ")" : "");
    }

    /**
     * Setter fluido para la IP.
     *
     * @param ip Dirección IP
     * @return Esta instancia para encadenar llamadas
     */
    public IPBan setIP(String ip) {
        this.ip = ip;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        IPBan ipBan = (IPBan) o;
        return Objects.equals(ip, ipBan.ip);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(ip);
    }
}
