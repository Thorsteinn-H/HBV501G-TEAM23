package is.hi.hbv501gteam23.Persistence.dto;

/**
 * Label-value pair format for metadata endpoints
 * @param label
 * @param value
 */
public record MetadataDto(
    String label,
    String value
) {}
