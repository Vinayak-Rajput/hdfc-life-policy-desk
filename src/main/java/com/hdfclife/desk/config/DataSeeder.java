package com.hdfclife.desk.config;

import com.hdfclife.desk.model.Policy;
import com.hdfclife.desk.service.PolicyService;
import com.hdfclife.desk.store.PolicyStore;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class DataSeeder implements CommandLineRunner {

    PolicyStore policyStore;
    HdfcProperties hdfcProperties;
    Environment environment;
    PolicyService policyService;

    public DataSeeder(PolicyStore policyStore, HdfcProperties hdfcProperties, PolicyService policyService, Environment environment) {
        this.policyStore = policyStore;
        this.hdfcProperties = hdfcProperties;
        this.policyService = policyService;
    }

    @Override
    public void run(String... args) throws Exception {

        PolicyService policyService = new PolicyService(policyStore);

        System.out.println("1. Active Profile -> " + environment.getProperty("spring.profiles.active"));

        System.out.println("2. Company name from HdfcProperties -> " + hdfcProperties.getCompanyName());

        System.out.println("3. Max claim amount -> " + hdfcProperties.getMaxClaimAmount());

        seedPolicies();

        System.out.println("4. Seeded policy count  -> " + policyService.getPolicies().size());

        System.out.println("5. Lookup HDFC-LIFE-1004 customer -> " + policyService.getPolicyByNo("HDFC-LIFE-1004").getCustomer());

        System.out.println("6. Active policy count via PolicyService -> " + policyService.getPoliciesByStatus("ACTIVE"));

        System.out.println("7. TERM policy count via PolicyService -> " + policyService.getPoliciesByType("TERM"));

        System.out.println("8. Unique customer count -> " + policyService.getPolicies().stream()
                .map(Policy::getCustomer)
                .distinct()
                .toList().size());

        System.out.println("9. Simple class name of the injected PolicyStore -> " + policyStore.getClass().getSimpleName());

        System.out.println("10. Lifecycle line (from @PostConstruct, may appear before the runner) -> " + environment.getProperty("spring.profiles.active"));

    }

    private void seedPolicies() {
        policyStore.add(new Policy("HDFC-LIFE-1001", "Anita Sharma", "TERM", 18500.0, "ACTIVE"));
        policyStore.add(new Policy("HDFC-LIFE-1002", "Rahul Mehta", "ULIP", 42000.0, "ACTIVE"));
        policyStore.add(new Policy("HDFC-LIFE-1003", "Priya Nair", "ENDOWMENT", 27000.0, "LAPSED"));
        policyStore.add(new Policy("HDFC-LIFE-1004", "Vikram Singh", "TERM", 15200.0, "ACTIVE"));
        policyStore.add(new Policy("HDFC-LIFE-1005", "Sneha Patel", "ULIP", 36000.0, "ACTIVE"));
        policyStore.add(new Policy("HDFC-LIFE-1006", "Anita Sharma", "ENDOWMENT", 22000.0, "PENDING"));
    }
}
