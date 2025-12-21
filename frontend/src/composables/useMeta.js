import { onMounted, onUnmounted } from 'vue';

export function useMeta(metaConfig) {
  const elements = [];

  const setTitle = (title) => {
    document.title = title;
  };

  const setMetaTag = (name, content, property = false) => {
    const attribute = property ? 'property' : 'name';
    let element = document.querySelector(`meta[${attribute}="${name}"]`);
    
    if (!element) {
      element = document.createElement('meta');
      element.setAttribute(attribute, name);
      document.head.appendChild(element);
      elements.push(element);
    }
    
    element.setAttribute('content', content);
  };

  onMounted(() => {
    if (metaConfig.title) {
      setTitle(metaConfig.title);
    }

    if (metaConfig.description) {
      setMetaTag('description', metaConfig.description);
      setMetaTag('og:description', metaConfig.description, true);
      setMetaTag('twitter:description', metaConfig.description);
    }

    if (metaConfig.ogTitle) {
      setMetaTag('og:title', metaConfig.ogTitle, true);
      setMetaTag('twitter:title', metaConfig.ogTitle);
    }

    if (metaConfig.ogImage) {
      setMetaTag('og:image', metaConfig.ogImage, true);
      setMetaTag('twitter:image', metaConfig.ogImage);
    }

    if (metaConfig.keywords) {
      setMetaTag('keywords', metaConfig.keywords);
    }

    if (metaConfig.url) {
      setMetaTag('og:url', metaConfig.url, true);
      
      // Update canonical link
      let canonical = document.querySelector('link[rel="canonical"]');
      if (!canonical) {
        canonical = document.createElement('link');
        canonical.setAttribute('rel', 'canonical');
        document.head.appendChild(canonical);
        elements.push(canonical);
      }
      canonical.setAttribute('href', metaConfig.url);
    }
  });

  onUnmounted(() => {
    // Clean up dynamically created elements if needed
    // elements.forEach(el => el.remove());
  });
}
