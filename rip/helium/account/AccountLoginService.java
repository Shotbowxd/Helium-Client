package rip.helium.account;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import rip.helium.Helium;
import rip.helium.utils.Mafs;

import java.net.Proxy;

/**
 * @author antja03
 */
public class AccountLoginService {

    private String status = "Idle";

    public void attemptLogin(Account account) {
        try {
            Helium.instance.altService.switchService(account.getService());

            if (account.getPassword().equals("")) {
                Minecraft.getMinecraft().session = new Session(account.getUsername(), "", "", "legacy");
                status = "Unable to Authenticate, Offline session started!: " + Minecraft.getMinecraft().session.getUsername();
                return;
            }

            YggdrasilUserAuthentication authentication = (YggdrasilUserAuthentication) new YggdrasilAuthenticationService(Proxy.NO_PROXY, "")
                    .createUserAuthentication(Agent.MINECRAFT);
            authentication.setUsername(account.getUsername());
            authentication.setPassword(account.getPassword());
            authentication.logIn();

            Minecraft.getMinecraft().session = new Session(authentication.getSelectedProfile().getName(), authentication.getSelectedProfile().getId().toString(),
                    authentication.getAuthenticatedToken(), "mojang");
            status = "Success! Authenticated with Username: " + Minecraft.getMinecraft().session.getUsername();

            return;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (AuthenticationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        status = "Login failed. [" + Mafs.getRandomInRange(1, 1337) + "]";
    }

    public String getStatus() {
        return status;
    }
}
