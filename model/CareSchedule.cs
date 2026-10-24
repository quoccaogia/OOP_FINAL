using System;

namespace vn.edu.crud.model;

public class CareSchedule
{
    public string CareId { get; set; } = string.Empty;
    public string FlowerId { get; set; } = string.Empty;
    public string EmployeeId { get; set; } = string.Empty;
    public DateTime? CheckDate { get; set; }
    public string FreshnessLevel { get; set; } = string.Empty;
    public string WaterStatus { get; set; } = string.Empty;
    public string ActionTaken { get; set; } = string.Empty;
    public string Note { get; set; } = string.Empty;

    public CareSchedule() { }
}
