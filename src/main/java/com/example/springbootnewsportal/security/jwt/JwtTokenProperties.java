    package com.example.springbootnewsportal.security.jwt;

    import lombok.Getter;
    import lombok.Setter;
    import org.springframework.boot.context.properties.ConfigurationProperties;
    import org.springframework.stereotype.Component;

    import java.time.Duration;

    @Component
    @ConfigurationProperties(prefix = "security.jwt")
    @Getter
    @Setter
    public class JwtTokenProperties {
        private Access access = new Access();
        private Refresh refresh = new Refresh();
        private String tokenPrefix;
        private String header;

        @Getter
        @Setter
        public static class Access {
            private String secret;
            private Duration expiration;
        }

        @Getter
        @Setter
        public static class Refresh {
            private String secret;
            private Duration expiration;
        }
    }
