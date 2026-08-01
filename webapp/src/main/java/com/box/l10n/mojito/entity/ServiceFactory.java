package com.box.l10n.mojito.entity;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;import org.springframework.stereotype.Component;

import com.box.l10n.mojito.entity.PullRunAssetService;
import com.box.l10n.mojito.entity.RepositoryService;
import com.box.l10n.mojito.entity.PullRunTextUnitVariantService;
import com.box.l10n.mojito.entity.TMTextUnitService;
import com.box.l10n.mojito.entity.AssetService;
import com.box.l10n.mojito.entity.PollableTaskService;
import com.box.l10n.mojito.entity.TranslationKitService;
import com.box.l10n.mojito.entity.security.user.UserService;
import com.box.l10n.mojito.entity.LocaleService;

@Component
public class ServiceFactory implements ApplicationContextAware {
    private static ConfigurableApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = (ConfigurableApplicationContext) context;
    }

    public static boolean isContainerAvailable() {
        return applicationContext != null && applicationContext.isActive();
    }

    public static PullRunAssetService getPullRunAssetService() {
        return applicationContext.getBean(PullRunAssetService.class);
    }

    public static RepositoryService getRepositoryService() {
        return applicationContext.getBean(RepositoryService.class);
    }

    public static PullRunTextUnitVariantService getPullRunTextUnitVariantService() {
        return applicationContext.getBean(PullRunTextUnitVariantService.class);
    }

    public static TMTextUnitService getTMTextUnitService() {
        return applicationContext.getBean(TMTextUnitService.class);
    }

    public static AssetService getAssetService() {
        return applicationContext.getBean(AssetService.class);
    }

	public static PollableTaskService getPollableTaskService() {
		return applicationContext.getBean(PollableTaskService.class);
	}

	public static TranslationKitService getTranslationKitService() {
		return applicationContext.getBean(TranslationKitService.class);
	}

	public static UserService getUserService() {
		return applicationContext.getBean(UserService.class);
	}

	public static LocaleService getLocaleService() {
		return applicationContext.getBean(LocaleService.class);
	}

}
